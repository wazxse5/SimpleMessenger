package wazxse5.server;

import wazxse5.common.UserInfo;
import wazxse5.common.message.Message;
import wazxse5.common.message.UserMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String surname;
    private String mail;
    private String login;
    private boolean guest;

    private boolean connected;
    private Connection connection;

    private List<User> friends;

    public User(String name, boolean guest, List<User> friends) {
        this.name = name;
        this.guest = guest;
        this.friends = friends;
    }

    public User(String name, boolean guest) {
        this(name, guest, new ArrayList<>());
    }

    public void send(Message message) {
        try {
            connection.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleReceivedMessage(UserMessage message) {
        for (User friend : friends) {
            if (friend.getName().equals(message.getTo())) {
                friend.send(message);
            }
        }
    }


    public void addFriend(User user) {
        friends.add(user);
    }

    public String getName() {
        return name;
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

    public boolean isGuest() {
        return guest;
    }

    public UserInfo getUserInfo() {
        return new UserInfo(name, surname, mail, login, guest);
    }
}
