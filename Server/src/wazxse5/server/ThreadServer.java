package wazxse5.server;

import wazxse5.common.message.Message;
import wazxse5.common.message.UserMessage;
import wazxse5.common.message.config.GoodbyeMessage;
import wazxse5.common.message.config.ServerMessage;
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
    private final List<User> connectedUsers;

    public ThreadServer(int port) {
        this.port = port;

        executor = Executors.newCachedThreadPool();

        dataLoader = new DataLoader();
        connectedUsers = new ArrayList<>();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            acceptingTask = new AcceptingTask(serverSocket, dataLoader);
            acceptingTask.valueProperty().addListener((observable, oldValue, newValue) -> addNewConnectedClient(newValue));
            acceptingTaskFuture = executor.submit(acceptingTask);

            UpdatingConnectedTask updatingConnectedTask = new UpdatingConnectedTask(this);
            updatingConnectedClientsFuture = executor.submit(updatingConnectedTask);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewConnectedClient(User user) {
        connectedUsers.add(user);
        ReceiveTask receiveTask = new ReceiveTask(user.getConnection().getInputStream());
        receiveTask.valueProperty().addListener((observable, oldValue, newValue) -> handleReceivedMessage(user, newValue));
        executor.submit(receiveTask);
    }

    private void handleReceivedMessage(User user, Message message) {
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage) message;
            user.handleReceivedMessage(userMessage);
        } else if (message instanceof ServerMessage) {
            if (message instanceof GoodbyeMessage) {
                user.setConnected(false);
                connectedUsers.remove(user);
                try {
                    user.getConnection().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<User> getConnectedUsers() {
        return connectedUsers;
    }

    public void close() {
        executor.shutdown();
        updatingConnectedClientsFuture.cancel(true);

        acceptingTaskFuture.cancel(true);
        acceptingTask.cancel(true);
    }
}
