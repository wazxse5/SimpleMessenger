package wazxse5.server;

import wazxse5.common.message.Message;
import wazxse5.common.message.config.LoginRequestMessage;
import wazxse5.common.message.config.RegisterRequestMessage;
import wazxse5.common.message.config.WelcomeMessage;
import wazxse5.server.task.AcceptingTask;
import wazxse5.server.task.LoginTask;
import wazxse5.server.task.ReceiveTask;
import wazxse5.server.task.UpdatingConnectedTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadServer {
    private final int port;

    private final ExecutorService executor;
    private AcceptingTask acceptingTask;
    private Future updatingConnectedTask;
    private final List<ReceiveTask> receiveTasks;

    private final DataLoader dataLoader;
    private final List<Connection> connectedConnections;

    public ThreadServer(int port) {
        this.port = port;
        this.executor = Executors.newCachedThreadPool();
        this.receiveTasks = new ArrayList<>();
        this.dataLoader = new DataLoader();
        this.connectedConnections = new ArrayList<>();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            acceptingTask = new AcceptingTask(serverSocket, dataLoader);
            acceptingTask.valueProperty().addListener((observable, oldValue, newValue) -> handleNewConnection(newValue));
            executor.submit(acceptingTask);

            UpdatingConnectedTask updatingConnectedTask = new UpdatingConnectedTask(this);
            this.updatingConnectedTask = executor.submit(updatingConnectedTask);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleNewConnection(Connection connection) {
        connectedConnections.add(connection);
        ReceiveTask receiveTask = new ReceiveTask(connection.getInputStream());
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
            dataLoader.register(registerRequestMessage.getUserInfo(), registerRequestMessage.getPassword());
        }
        if (message instanceof LoginRequestMessage) {
            LoginRequestMessage loginRequestMessage = (LoginRequestMessage) message;
            LoginTask loginTask = new LoginTask(dataLoader, connection, loginRequestMessage);
            executor.submit(loginTask);
        }
    }

    public List<User> getConnectedUsers() {
        List<User> connectedUsers = new ArrayList<>(connectedConnections.size());
        for (Connection c : connectedConnections) connectedUsers.add(c.getUser());
        return connectedUsers;
    }

    public void close() {
        executor.shutdown();
        if (acceptingTask != null) acceptingTask.cancel(true);
        if (updatingConnectedTask != null) updatingConnectedTask.cancel(true);
        for (ReceiveTask receiveTask : receiveTasks) receiveTask.cancel(true);
        for (Connection connection : connectedConnections) connection.close();
    }
}
