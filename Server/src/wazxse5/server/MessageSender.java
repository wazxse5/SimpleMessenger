package wazxse5.server;

import wazxse5.common.message.Message;
import wazxse5.common.message.config.ConfirmationMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageSender {
    private Map<Message, Connection> messagesToSend;
    private ExecutorService executor;

    public MessageSender() {
        this.messagesToSend = new ConcurrentHashMap<>();
        this.executor = Executors.newSingleThreadExecutor();
        executor.execute(new SendingTask());
    }

    public void addMessageToSend(Message message, Connection connection) {
        messagesToSend.put(message, connection);
        connection.send(message);
    }

    public void handleConfirmationMessage(ConfirmationMessage confirmationMessage) {
        int confirmedId = confirmationMessage.getConfirmedId();
        for (Message message : messagesToSend.keySet()) {
            if (message.getId() == confirmedId) messagesToSend.remove(message);
        }
    }

    public List<Message> finish() {
        executor.shutdownNow();
        return new ArrayList<>(messagesToSend.keySet());
    }

    private class SendingTask implements Runnable {
        @Override public void run() {
            try {
                for (Map.Entry<Message, Connection> entry : messagesToSend.entrySet()) {
                    entry.getValue().send(entry.getKey());
                }
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
