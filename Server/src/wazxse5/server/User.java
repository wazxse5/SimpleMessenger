package wazxse5.server;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import wazxse5.common.UserInfo;
import wazxse5.common.message.Message;
import wazxse5.common.message.UserMessage;

import java.util.List;

public class User {
    private StringProperty id = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty surname = new SimpleStringProperty();
    private StringProperty login = new SimpleStringProperty();
    private StringProperty mail = new SimpleStringProperty();

    private BooleanProperty connected = new SimpleBooleanProperty();
    private Connection connection;
    private UserInfo userInfo;

    private List<User> friends;

    public User(UserInfo userInfo) {
        id.setValue(Integer.toString(userInfo.getId()));
        if (userInfo.getId() == -1) id.setValue("G");
        name.setValue(userInfo.getName());
        surname.setValue(userInfo.getSurname());
        login.setValue(userInfo.getLogin());
        mail.setValue(userInfo.getMail());
        this.userInfo = userInfo;
        this.connected.setValue(false);
        this.connection = null;
    }

    private User() {
    }

    public static User createEmptyUser() {
        return new User();
    }

    public static User createGuest(String login) {
        return new User(new UserInfo(login, true));
    }

    public void send(Message message) {
        connection.send(message);
    }

    public void handleReceivedMessage(UserMessage message) {
        for (User friend : friends) {
            if (friend.getName().equals(message.getTo())) {
                friend.send(message);
            }
        }
    }

    public boolean isConnected() {
        return connected.get();
    }

    public BooleanProperty connectedProperty() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected.set(connected);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public String getLogin() {
        return login.get();
    }

    public StringProperty loginProperty() {
        return login;
    }

    public String getMail() {
        return mail.get();
    }

    public StringProperty mailProperty() {
        return mail;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
