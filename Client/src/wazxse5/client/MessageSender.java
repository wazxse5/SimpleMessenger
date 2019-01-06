package wazxse5.client;

import wazxse5.common.message.Message;
import wazxse5.common.message.config.ConfirmationMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageSender {
    private Connection connection;
    private List<Message> messagesToSend;
    private ExecutorService executor;
    private boolean paused;

    public MessageSender() {
        this.paused = false;
        this.messagesToSend = new ArrayList<>();
        this.executor = Executors.newSingleThreadExecutor();
        executor.execute(new SendingTask());
    }

    public void addMessageToSend(Message message) {
        messagesToSend.add(message);
        connection.send(message);
    }

    public void handleConfirmationMessage(ConfirmationMessage confirmationMessage) {
        int confirmedId = confirmationMessage.getConfirmedId();
        for (Message message : messagesToSend) {
            if (message.getId() == confirmedId) messagesToSend.remove(message);
        }
    }

    public void pause() {
        paused = true;
    }

    public void start() {
        paused = false;
    }

    public List<Message> finish() {
        executor.shutdownNow();
        return new ArrayList<>(messagesToSend);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private class SendingTask implements Runnable {
        @Override public void run() {
            try {
                if (!paused) {
                    for (Message message : messagesToSend) {
                        connection.send(message);
                    }
                }
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
