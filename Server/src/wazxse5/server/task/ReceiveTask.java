package wazxse5.server.task;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import wazxse5.common.message.Message;
import wazxse5.common.message.UserMessage;
import wazxse5.common.message.config.ServerMessage;
import wazxse5.server.Connection;

import java.io.ObjectInputStream;

public class ReceiveTask extends Task<Message> {
    private ObservableList<Connection> connectedConnections;
    private final ObjectInputStream input;

    public ReceiveTask(ObservableList<Connection> connectedConnections, ObjectInputStream input) {
        this.connectedConnections = connectedConnections;
        this.input = input;
    }

    @Override protected Message call() throws Exception {
        while (!isCancelled()) {
            Object receiveObject = input.readObject();
            if (receiveObject instanceof ServerMessage) {
                ServerMessage serverMessage = (ServerMessage) receiveObject;
                updateValue(serverMessage);
            } else if (receiveObject instanceof UserMessage) {
                UserMessage userMessage = (UserMessage) receiveObject;
                if (userMessage.getTo().equals("-%&-all")) {
                    for (Connection connection : connectedConnections)
                        if (!connection.getUser().getLogin().equals(userMessage.getFrom()))
                            connection.send(userMessage);
                } else {
                    Connection connection = findAddressee(userMessage.getTo());
                    if (connection != null) connection.send(userMessage);
                }
            }
        }
        return null;
    }

    private Connection findAddressee(String login) {
        for (Connection connection : connectedConnections) {
            if (connection.isLogged() && connection.getUser().getLogin().equals(login)) return connection;
        }
        return null;
    }
}
