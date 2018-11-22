package wazxse5.client;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import message.Message;
import message.UserMessage;
import message.config.GoodbyeMessage;
import message.config.SessionMessage;
import wazxse5.client.controller.MainController;
import wazxse5.client.task.LoginTask;
import wazxse5.client.task.ReceiveTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadClient {
    private final String host;
    private final int port;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private String userName;
    private ListProperty<String> connectedFriendsProperty = new SimpleListProperty<>();
    private ObservableList<String> connectedFriendsList = FXCollections.observableList(new ArrayList<>());
    private MainController mainController;

    private ExecutorService executor;
    private ReceiveTask receiveTask;


    public ThreadClient(String host, int port) {
        this.host = host;
        this.port = port;
        connectedFriendsProperty.setValue(connectedFriendsList);
    }

    public boolean connect(String name, String password, boolean asGuest) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        socket = new Socket(host, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

        executor = Executors.newSingleThreadExecutor();
        LoginTask loginTask = new LoginTask(input, output, name, password, asGuest);
        Future<Boolean> future = executor.submit(loginTask);

        try {
            boolean connectionResult = future.get(3, TimeUnit.SECONDS);
            if (connectionResult) userName = name;
            return connectionResult;
        } catch (TimeoutException timeoutException) {
            future.cancel(true);
            throw timeoutException;
        }
    }

    public void start() {
        receiveTask = new ReceiveTask(input);
        receiveTask.valueProperty().addListener((observable, oldValue, newValue) -> handleReceivedMessage(newValue));
        executor.execute(receiveTask);
    }

    public void send(String to, String message) {
        try {
            output.writeObject(new UserMessage(userName, to, message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleReceivedMessage(Message message) {
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage) message;
            mainController.handleReceivedMessage(userMessage.getFrom(), userMessage.getMessage());
        }
        if (message instanceof SessionMessage) {
            SessionMessage sessionMessage = (SessionMessage) message;
            updateConnectedFriends(sessionMessage.getConncectedClientsNames());
        }
    }

    private void updateConnectedFriends(List<String> currentlyConnectedFriends) {
        Platform.runLater(() -> {
            for (String friend : currentlyConnectedFriends) {
                if (!connectedFriendsList.contains(friend)) connectedFriendsList.add(friend);
            }
            for (String friend : connectedFriendsList) {
                if (!currentlyConnectedFriends.contains(friend)) connectedFriendsList.remove(friend);
            }
        });
    }

    public ListProperty<String> connectedFriendsProperty() {
        return connectedFriendsProperty;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void close() {
        try {
            output.writeObject(new GoodbyeMessage());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        receiveTask.cancel(true);
        executor.shutdown();
    }

}
