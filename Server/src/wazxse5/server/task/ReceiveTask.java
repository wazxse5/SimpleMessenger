package wazxse5.server.task;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import wazxse5.common.message.Message;
import wazxse5.common.message.UserMessage;
import wazxse5.common.message.config.ConfirmationMessage;
import wazxse5.common.message.config.ServerMessage;
import wazxse5.server.Connection;
import wazxse5.server.MessageSender;

import java.io.ObjectInputStream;

public class ReceiveTask extends Task<Message> {
    private ObservableList<Connection> connectedConnections;
    private final ObjectInputStream input;
    private MessageSender messageSender;

    public ReceiveTask(ObservableList<Connection> connectedConnections, ObjectInputStream input, MessageSender messageSender) {
        this.connectedConnections = connectedConnections;
        this.input = input;
        this.messageSender = messageSender;
    }

    @Override protected Message call() throws Exception {
        while (!isCancelled()) {
            Object receiveObject = input.readObject();
            if (receiveObject instanceof ConfirmationMessage) {
                ConfirmationMessage confirmationMessage = (ConfirmationMessage) receiveObject;
                messageSender.handleConfirmationMessage(confirmationMessage);
            } else if (receiveObject instanceof UserMessage) {
                UserMessage userMessage = (UserMessage) receiveObject;
                handleUserMessage(userMessage);
            } else if (receiveObject instanceof ServerMessage) {
                ServerMessage serverMessage = (ServerMessage) receiveObject;
                updateValue(serverMessage);
            }
        }
        return null;
    }

    private void handleUserMessage(UserMessage userMessage) {
        if (userMessage.getTo().equals("-%&-all")) {
            for (Connection connection : connectedConnections) {
                if (!connection.getUser().getLogin().equals(userMessage.getFrom())) {
                    messageSender.addMessageToSend(userMessage, connection);
                }
            }
        } else {
            Connection connection = findAddressee(userMessage.getTo());
            if (connection != null) messageSender.addMessageToSend(userMessage, connection);
        }
    }

    private Connection findAddressee(String login) {
        for (Connection connection : connectedConnections) {
            if (connection.isLogged() && connection.getUser().getLogin().equals(login)) return connection;
        }
        return null;
    }
}
