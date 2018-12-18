package wazxse5.client;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import wazxse5.client.task.ConnectTask;
import wazxse5.client.task.ReceiveTask;
import wazxse5.common.UserInfo;
import wazxse5.common.exception.AuthenticationException;
import wazxse5.common.message.Message;
import wazxse5.common.message.UserMessage;
import wazxse5.common.message.config.LoginAnswerMessage;
import wazxse5.common.message.config.LoginRequestMessage;
import wazxse5.common.message.config.RegisterRequestMessage;
import wazxse5.common.message.config.SessionMessage;

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

    public void connect(String address, int port) {
        ConnectTask connectTask = new ConnectTask(address, port);
        connectTask.setOnSucceeded(event -> handleConnectionSucceed(event.getSource()));
        connectTask.setOnFailed(event -> handleConnectionFailed(event.getSource()));
        executor.execute(connectTask);
    }

    public void sendLoginRequest(String address, int port, String login, byte[] password, boolean guest) {
        if (connection == null) connect(address, port);
        if (connection != null) {
            connection.send(new LoginRequestMessage(login, password, guest));
        }
    }

    public void sendRegisterRequest(String address, int port, UserInfo userInfo, byte[] password) {
        if (connection == null) connect(address, port);
        if (connection != null) {
            connection.send(new RegisterRequestMessage(userInfo, password));
        }
    }

    private void handleConnectionSucceed(Worker workerConnectTask) {
        Object objectConnection = workerConnectTask.getValue();
        if (objectConnection != null) {
            connection = (Connection) objectConnection;
            receiveTask = new ReceiveTask(connection.getInput());
            receiveTask.valueProperty().addListener((observable, oldValue, newValue) -> handleReceivedMessage(newValue));
            executor.execute(receiveTask);
        }
    }

    private void handleConnectionFailed(Worker workerConnectTask) {
        viewManager.handleLoginError(workerConnectTask.getException());
    }

    public void send(String to, String message) {
        connection.send(new UserMessage(connection.getUserInfo().getLogin(), to, message));
    }

    private void handleReceivedMessage(Message message) {
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage) message;
        }
        if (message instanceof SessionMessage) {
            SessionMessage sessionMessage = (SessionMessage) message;
            updateConnectedFriends(sessionMessage.getConncectedClientsNames());
        }
        if (message instanceof LoginAnswerMessage) {
            LoginAnswerMessage loginAnswerMessage = (LoginAnswerMessage) message;
            if (loginAnswerMessage.isGood()) connection.setUserInfo(loginAnswerMessage.getUserInfo());
            else try {
                throw loginAnswerMessage.getException();
            } catch (AuthenticationException e) {
                e.printStackTrace();
            }
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
        if (connection != null) connection.close();
        if (receiveTask != null) receiveTask.cancel(true);
        if (executor != null) executor.shutdown();
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}
