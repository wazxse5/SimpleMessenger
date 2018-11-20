package wazxse5.client;

import javafx.beans.property.ReadOnlyStringProperty;
import message.UserMessage;
import wazxse5.client.task.LoginTask;
import wazxse5.client.task.ReceiveTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.*;

public class ThreadClient {
    private final String host;
    private final int port;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private String userName;

    private ExecutorService executor;
    private ReceiveTask receiveTask;


    public ThreadClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect(String name, String password, boolean asGuest) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        socket = new Socket(host, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

        executor = Executors.newSingleThreadExecutor();
        LoginTask loginTask = new LoginTask(input, output, name, password, asGuest);
        Future<Boolean> future = executor.submit(loginTask);

        boolean connectionResult = future.get(3, TimeUnit.SECONDS);
        if (connectionResult) userName = name;
        return connectionResult;
    }

    public void start() {
        receiveTask = new ReceiveTask(input);
        executor.execute(receiveTask);
    }

    public void send(String to, String message) {
        try {
            output.writeObject(new UserMessage(userName, to, message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        receiveTask.cancel(true);
        executor.shutdown();
    }

    public ReadOnlyStringProperty receiveProperty() {
        return receiveTask.messageProperty();
    }

}
