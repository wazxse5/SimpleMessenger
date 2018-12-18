package wazxse5.server;

import wazxse5.common.message.Message;
import wazxse5.common.message.config.RegisterRequestMessage;
import wazxse5.common.message.config.WelcomeMessage;
import wazxse5.server.task.AcceptingTask;
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
    private Future acceptingTaskFuture;
    private AcceptingTask acceptingTask;
    private Future updatingConnectedClientsFuture;

    private final DataLoader dataLoader;
    private final List<Connection> connectedConnections;

    public ThreadServer(int port) {
        this.port = port;
        this.dataLoader = new DataLoader();
        this.connectedConnections = new ArrayList<>();
        this.executor = Executors.newCachedThreadPool();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            acceptingTask = new AcceptingTask(serverSocket, dataLoader);
            acceptingTask.valueProperty().addListener((observable, oldValue, newValue) -> handleNewConnection(newValue));
            acceptingTaskFuture = executor.submit(acceptingTask);

            UpdatingConnectedTask updatingConnectedTask = new UpdatingConnectedTask(this);
            updatingConnectedClientsFuture = executor.submit(updatingConnectedTask);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleNewConnection(Connection connection) {
        connectedConnections.add(connection);
        ReceiveTask receiveTask = new ReceiveTask(connection.getInputStream());
        receiveTask.valueProperty().addListener((observable, oldValue, newValue) -> handleReceivedMessage(connection, newValue));
        executor.submit(receiveTask);
    }

    private void handleReceivedMessage(Connection connection, Message message) {
        if (message instanceof WelcomeMessage) {
            connection.send(message);
        }
        if (message instanceof RegisterRequestMessage) {
            System.out.println("ThreadServer.handleReceivedMessage.RegisterRequest");
            RegisterRequestMessage registerRequestMessage = (RegisterRequestMessage) message;
            dataLoader.register(registerRequestMessage.getUserInfo(), registerRequestMessage.getPassword());
        }
    }

    public List<User> getConnectedUsers() {
        List<User> connectedUsers = new ArrayList<>(connectedConnections.size());
        for (Connection c : connectedConnections) connectedUsers.add(c.getUser());
        return connectedUsers;
    }

    public void close() {
        executor.shutdown();
        updatingConnectedClientsFuture.cancel(true);

        acceptingTaskFuture.cancel(true);
        acceptingTask.cancel(true);
    }
}
