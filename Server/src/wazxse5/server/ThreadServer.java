package wazxse5.server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wazxse5.common.message.Message;
import wazxse5.common.message.UserMessage;
import wazxse5.common.message.config.GoodbyeMessage;
import wazxse5.common.message.config.LoginRequestMessage;
import wazxse5.common.message.config.RegisterRequestMessage;
import wazxse5.common.message.config.WelcomeMessage;
import wazxse5.server.task.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadServer {
    private ViewManager viewManager;
    private int serverPort;

    private final ExecutorService executor;
    private AcceptingTask acceptingTask;
    private Future updatingConnectedTask;
    private final List<ReceiveTask> receiveTasks;
    private MessageSender messageSender;

    private final DataLoader dataLoader;
    private final ObservableList<Connection> connectedConnections;
    private final ObservableList<String> loggedUsersLogins;
    private final List<UserMessage> sentUserMessages;

    public ThreadServer() {
        this.executor = Executors.newCachedThreadPool();
        this.receiveTasks = new ArrayList<>();
        this.messageSender = new MessageSender();
        this.connectedConnections = FXCollections.observableArrayList();
        this.loggedUsersLogins = FXCollections.observableArrayList();
        this.dataLoader = new DataLoader(loggedUsersLogins);
        this.sentUserMessages = new ArrayList<>();
    }

    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            this.serverPort = port;

            acceptingTask = new AcceptingTask(serverSocket, dataLoader);
            acceptingTask.valueProperty().addListener((observable, oldValue, newValue) -> handleNewConnection(newValue));
            executor.submit(acceptingTask);

            UpdatingConnectedTask updatingConnectedTask = new UpdatingConnectedTask(connectedConnections, loggedUsersLogins);
            this.updatingConnectedTask = executor.submit(updatingConnectedTask);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleNewConnection(Connection connection) {
        connection.setViewManager(viewManager);
        connectedConnections.add(connection);
        ReceiveTask receiveTask = new ReceiveTask(connectedConnections, connection.getInputStream(), messageSender);
        receiveTask.valueProperty().addListener((observable, oldValue, newValue) -> handleReceivedMessage(connection, newValue));
        receiveTasks.add(receiveTask);
        executor.submit(receiveTask);
    }

    private void handleReceivedMessage(Connection connection, Message message) {
        if (message instanceof WelcomeMessage) {
            connection.send(message);
        }
        if (message instanceof RegisterRequestMessage) {
            RegisterRequestMessage registerRequestMessage = (RegisterRequestMessage) message;
            RegisterTask registerTask = new RegisterTask(dataLoader, connection, registerRequestMessage);
            executor.submit(registerTask);
        }
        if (message instanceof LoginRequestMessage) {
            LoginRequestMessage loginRequestMessage = (LoginRequestMessage) message;
            LoginTask loginTask = new LoginTask(dataLoader, connection, loginRequestMessage);
            loginTask.messageProperty().addListener((observable, oldValue, newValue) -> loggedUsersLogins.add(newValue));
            executor.submit(loginTask);
        }
        if (message instanceof GoodbyeMessage) {
            GoodbyeMessage goodbyeMessage = (GoodbyeMessage) message;
            if (goodbyeMessage.getMessage().equals("logout")) {
                loggedUsersLogins.remove(connection.getUser().getLogin());
                connection.setUser(User.createEmptyUser());
                connection.setLogged(false);
            } else {
                loggedUsersLogins.remove(connection.getUser().getLogin());
                connectedConnections.remove(connection);
            }
        }
    }

    public ObservableList<Connection> getConnectedConnections() {
        return connectedConnections;
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public int getServerPort() {
        return serverPort;
    }

    public DataLoader getDataLoader() {
        return dataLoader;
    }

    public void close() {
        if (messageSender != null) messageSender.finish();
        if (executor != null) executor.shutdown();
        if (acceptingTask != null) acceptingTask.cancel(true);
        if (updatingConnectedTask != null) updatingConnectedTask.cancel(true);
        for (ReceiveTask receiveTask : receiveTasks) receiveTask.cancel(true);
        for (Connection connection : connectedConnections) connection.close();
    }
}
