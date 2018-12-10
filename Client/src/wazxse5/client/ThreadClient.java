package wazxse5.client;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import message.Message;
import message.UserMessage;
import message.config.SessionMessage;
import wazxse5.client.task.ConnectTask;
import wazxse5.client.task.ReceiveTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadClient {
    private ViewManager viewManager;
    private Connection connection;

    private ListProperty<String> connectedFriendsProperty = new SimpleListProperty<>();
    private ObservableList<String> connectedFriendsList = FXCollections.observableList(new ArrayList<>());

    private ExecutorService executor;
    private ReceiveTask receiveTask;

    public ThreadClient() {
        executor = Executors.newSingleThreadExecutor();
        connectedFriendsProperty.setValue(connectedFriendsList);
    }

    public void connect(String address, int port, String login, String password, boolean guest) {
        ConnectTask connectTask = new ConnectTask(address, port, login, password, guest);
        connectTask.setOnSucceeded(event -> handleConnection((Connection) event.getSource().getValue()));
        executor.execute(connectTask);
    }

    private void handleConnection(Connection newConnection) {
        if (newConnection != null) {
            connection = newConnection;
            receiveTask = new ReceiveTask(newConnection.getInput());
            receiveTask.valueProperty().addListener((observable, oldValue, newValue) -> handleReceivedMessage(newValue));
            executor.execute(receiveTask);
        }
    }

    public void send(String to, String message) {
        connection.send(new UserMessage(connection.getUser().getLogin(), to, message));
    }

    private void handleReceivedMessage(Message message) {
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage) message;
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

    public void close() {
        connection.close();
        receiveTask.cancel(true);
        executor.shutdown();
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}
