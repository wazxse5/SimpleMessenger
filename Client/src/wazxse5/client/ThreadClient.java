package wazxse5.client;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wazxse5.client.task.ConnectTask;
import wazxse5.client.task.ReceiveTask;
import wazxse5.common.UserInfo;
import wazxse5.common.exception.DatabaseException;
import wazxse5.common.exception.NoConnectionException;
import wazxse5.common.message.Message;
import wazxse5.common.message.UserMessage;
import wazxse5.common.message.config.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadClient {
    private ViewManager viewManager;
    private Connection connection;
    private ExecutorService executor;
    private ReceiveTask receiveTask;
    private MessageSender messageSender;

    private BooleanProperty connected = new SimpleBooleanProperty();
    private ListProperty<String> connectedFriendsProperty = new SimpleListProperty<>();
    private ObservableList<String> loggedUserNamesList = FXCollections.observableList(new ArrayList<>());


    public ThreadClient() {
        this.executor = Executors.newSingleThreadExecutor();
        this.connectedFriendsProperty.setValue(loggedUserNamesList);
        this.messageSender = new MessageSender();
    }

    public void connect(String address, int port) {
        ConnectTask connectTask = new ConnectTask(address, port);
        connectTask.setOnSucceeded(event -> handleConnectionSucceed(event.getSource().getValue()));
        connectTask.setOnFailed(event -> viewManager.handleConnectError(event.getSource().getException()));
        executor.execute(connectTask);
    }

    private void handleConnectionSucceed(Object objectConnection) {
        if (objectConnection != null) {
            connection = (Connection) objectConnection;
            messageSender.setConnection(connection);
            messageSender.start();
            connected.setValue(true);
            receiveTask = new ReceiveTask(connection.getInput(), messageSender);
            receiveTask.valueProperty().addListener((observable, oldValue, newValue) -> handleReceivedMessage(newValue));
            executor.execute(receiveTask);
        }
    }

    private void handleReceivedMessage(Message message) {
        if (message instanceof SessionMessage) {
            SessionMessage sessionMessage = (SessionMessage) message;
            updateLoggedUserNames(sessionMessage.getLoggedUserNames());
        }
        if (message instanceof LoginAnswerMessage) {
            LoginAnswerMessage loginAnswerMessage = (LoginAnswerMessage) message;
            if (loginAnswerMessage.isGood()) {
                connection.setUserInfo(loginAnswerMessage.getUserInfo());
                viewManager.loadMainScene(loginAnswerMessage.getUserInfo());
            } else viewManager.handleLoginError(loginAnswerMessage.getException());
        }
        if (message instanceof RegisterAnswerMessage) {
            RegisterAnswerMessage registerAnswerMessage = (RegisterAnswerMessage) message;
            viewManager.handleRegisterError(registerAnswerMessage.getException());
        }
        if (message instanceof GoodbyeMessage) {
            GoodbyeMessage goodbyeMessage = (GoodbyeMessage) message;
            viewManager.handleReceivedGoodbyeMessage(goodbyeMessage);
        }
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage) message;
            viewManager.handleReceivedUserMessage(userMessage);
        }
    }

    public void sendLoginRequest(String login, String password, boolean guest) {
        if (connection != null) {
            connection.send(new LoginRequestMessage(login, password, guest));
        } else viewManager.handleLoginError(new DatabaseException());
    }

    public void sendRegisterRequest(UserInfo userInfo, String password) {
        if (connection != null) {
            connection.send(new RegisterRequestMessage(userInfo, password));
        } else viewManager.handleRegisterError(new NoConnectionException());
    }

    public void sendUserMessage(String to, String message) {
        messageSender.addMessageToSend(new UserMessage(connection.getUserInfo().getLogin(), to, message));
    }

    public void logout() {
        connection.send(new GoodbyeMessage("logout"));
        viewManager.loadInitScene();
    }

    public void disconnect() {
        messageSender.pause();
        receiveTask.cancel(true);
        connection.close();
        connection = null;
        connected.setValue(false);
    }

    public void close() {
        if (messageSender != null) messageSender.finish(); // TODO: Zapisanie niedostarczonych wiadomości do pliku
        if (connection != null) connection.send(new GoodbyeMessage("exit"));
        if (executor != null) executor.shutdown();
        if (receiveTask != null) receiveTask.cancel(true);
        if (connection != null) connection.close();
    }

    private void updateLoggedUserNames(List<String> loggedUserNames) {
        Platform.runLater(() -> {
            for (String friend : loggedUserNames) {
                if (!loggedUserNamesList.contains(friend)) loggedUserNamesList.add(friend);
            }
            for (String friend : loggedUserNamesList) {
                if (!loggedUserNames.contains(friend) && !friend.equals("Publiczne"))
                    loggedUserNamesList.remove(friend);
            }
            loggedUserNamesList.remove(connection.getUserInfo().getLogin());
        });
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public ListProperty<String> connectedFriendsProperty() {
        return connectedFriendsProperty;
    }

    public BooleanProperty connectedProperty() {
        return connected;
    }
}
