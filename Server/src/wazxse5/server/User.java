package wazxse5.server;

import wazxse5.common.UserInfo;
import wazxse5.common.message.Message;
import wazxse5.common.message.UserMessage;

import java.util.List;

public class User {
    private UserInfo userInfo;
    private boolean connected;
    private Connection connection;

    private List<User> friends;

    public User(UserInfo userInfo) {
        this.userInfo = userInfo;
        this.connected = false;
        this.connection = null;
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

    public String getName() {
        return userInfo.getName();
    }


    public void addFriend(User user) {
        friends.add(user);
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    public UserInfo getUserInfo() {
        return userInfo;
    }
}
