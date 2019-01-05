package wazxse5.client.task;

import javafx.concurrent.Task;
import wazxse5.client.MessageSender;
import wazxse5.common.message.Message;
import wazxse5.common.message.config.ConfirmationMessage;

import java.io.ObjectInputStream;

public class ReceiveTask extends Task<Message> {
    private final ObjectInputStream input;
    private MessageSender messageSender;

    public ReceiveTask(ObjectInputStream input, MessageSender messageSender) {
        this.input = input;
        this.messageSender = messageSender;
    }

    @Override protected Message call() throws Exception {
        while (!isCancelled()) {
            Object receiveObject = input.readObject();
            if (receiveObject instanceof Message) {
                Message message = (Message) receiveObject;
                updateValue(message);

                if (message instanceof ConfirmationMessage) {
                    ConfirmationMessage confirmationMessage = (ConfirmationMessage) message;
                    messageSender.handleConfirmationMessage(confirmationMessage);
                }
            }
        }
        return null;
    }
}
