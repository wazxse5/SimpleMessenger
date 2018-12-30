package wazxse5.client;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;
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

    private BooleanProperty connected = new SimpleBooleanProperty();
    private ListProperty<String> connectedFriendsProperty = new SimpleListProperty<>();
    private ObservableList<String> loggedUserNamesList = FXCollections.observableList(new ArrayList<>());


    public ThreadClient() {
        executor = Executors.newSingleThreadExecutor();
        connectedFriendsProperty.setValue(loggedUserNamesList);
    }

    public void connect(String address, int port) {
        ConnectTask connectTask = new ConnectTask(address, port);
        connectTask.setOnSucceeded(event -> handleConnectionSucceed(event.getSource()));
        connectTask.setOnFailed(event -> handleConnectionFailed(event.getSource()));
        executor.execute(connectTask);
    }

    public void sendLoginRequest(String login, byte[] password, boolean guest) {
        if (connection != null) {
            connection.send(new LoginRequestMessage(login, password, guest));
        } else viewManager.handleLoginError(new DatabaseException());
    }

    public void sendRegisterRequest(UserInfo userInfo, byte[] password) {
        if (connection != null) {
            connection.send(new RegisterRequestMessage(userInfo, password));
        } else viewManager.handleRegisterError(new NoConnectionException());
    }

    private void handleConnectionSucceed(Worker workerConnectTask) {
        Object objectConnection = workerConnectTask.getValue();
        if (objectConnection != null) {
            connection = (Connection) objectConnection;
            connected.setValue(true);
            receiveTask = new ReceiveTask(connection.getInput());
            receiveTask.valueProperty().addListener((observable, oldValue, newValue) -> handleReceivedMessage(newValue));
            executor.execute(receiveTask);
        }
    }

    private void handleConnectionFailed(Worker workerConnectTask) {
        viewManager.handleConnectError(workerConnectTask.getException());
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
            updateLoggedUserNames(sessionMessage.getLoggedUserNames());
        }
        if (message instanceof LoginAnswerMessage) {
            LoginAnswerMessage loginAnswerMessage = (LoginAnswerMessage) message;
            if (loginAnswerMessage.isGood()) {
                connection.setUserInfo(loginAnswerMessage.getUserInfo());
                viewManager.loadMainScene(loginAnswerMessage.getUserInfo().getSimpleInfo());
            } else viewManager.handleLoginError(loginAnswerMessage.getException());
        }
        if (message instanceof RegisterAnswerMessage) {
            RegisterAnswerMessage registerAnswerMessage = (RegisterAnswerMessage) message;
            if (registerAnswerMessage.isGood()) viewManager.getInitController().setInfoText("R", "Zarejestrowano");
            else viewManager.handleRegisterError(registerAnswerMessage.getException());
        }
        if (message instanceof GoodbyeMessage) {
            GoodbyeMessage goodbyeMessage = (GoodbyeMessage) message;
            if (goodbyeMessage.getMessage().equals("exit")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ostrzeżenie");
                alert.setHeaderText(null);
                alert.setContentText("Serwer rozłączył się");
                alert.showAndWait();
            }
        }
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage) message;
            viewManager.handleReceivedUserMessage(userMessage);
        }
    }

    private void updateLoggedUserNames(List<String> loggedUserNames) {
        Platform.runLater(() -> {
            for (String friend : loggedUserNames) {
                if (!loggedUserNamesList.contains(friend)) loggedUserNamesList.add(friend);
            }
            for (String friend : loggedUserNamesList) {
                if (!loggedUserNames.contains(friend)) loggedUserNamesList.remove(friend);
            }
            loggedUserNamesList.remove(connection.getUserInfo().getLogin());
        });
    }

    public ListProperty<String> connectedFriendsProperty() {
        return connectedFriendsProperty;
    }

    public BooleanProperty connectedProperty() {
        return connected;
    }

    public void logout() {
        connection.send(new GoodbyeMessage("logout"));
        viewManager.loadInitScene();
    }

    public void close() {
        if (connection != null) connection.send(new GoodbyeMessage("exit"));
        if (executor != null) executor.shutdown();
        if (receiveTask != null) receiveTask.cancel(true);
        if (connection != null) connection.close();
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}
