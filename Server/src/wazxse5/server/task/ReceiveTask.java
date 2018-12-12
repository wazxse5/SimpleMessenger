package wazxse5.server.task;

import javafx.concurrent.Task;
import wazxse5.common.message.Message;

import java.io.ObjectInputStream;

public class ReceiveTask extends Task<Message> {
    private final ObjectInputStream input;

    public ReceiveTask(ObjectInputStream input) {
        this.input = input;
    }

    @Override protected Message call() throws Exception {
        while (!isCancelled()) {
            Object receiveObject = input.readObject();
            if (receiveObject instanceof Message) {
                Message message = (Message) receiveObject;
                updateValue(message);
            }
        }
        return null;
    }
}
