package wazxse5.client;

import javafx.beans.property.ReadOnlyStringProperty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadClient {
    private String host;
    private int port;

    private Socket socket;
    private ReceiveTask receiveTask;
    private PrintWriter output;

    public ThreadClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        InputStream inputStream = socket.getInputStream();
        receiveTask = new ReceiveTask(inputStream);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(receiveTask);
        executor.shutdown();

        OutputStream outputStream = socket.getOutputStream();
        output = new PrintWriter(outputStream, true);
    }

    public void send(String message) {
        output.println(message);
    }

    public void disconnect() {
        output.println("na razie");
        receiveTask.cancel();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ReadOnlyStringProperty receiveProperty() {
        return receiveTask.messageProperty();
    }

}
