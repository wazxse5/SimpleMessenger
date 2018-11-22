package wazxse5.server;

import message.Message;
import message.UserMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private String name;
    private boolean guest;

    private boolean connected;
    private Connection connection;

    private List<Client> friends;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public Client(String name, boolean guest, List<Client> friends) {
        this.name = name;
        this.guest = guest;
        this.friends = friends;
    }

    public Client(String name, boolean guest) {
        this(name, guest, new ArrayList<>());
    }

    public void send(Message message) {
        try {
            connection.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleReceivedMessage(Message message) {
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage) message;
            for (Client friend : friends) {
                if (friend.getName().equals(userMessage.getTo())) {
                    friend.send(message);
                }
            }

        }
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
}
