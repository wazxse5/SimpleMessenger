package wazxse5.client;

import javafx.beans.property.ReadOnlyStringProperty;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

public class ThreadClient {
    private String host;
    private int port;
    private Socket socket;

    private Scanner input;
    private PrintWriter output;

    private ExecutorService executor;
    private ReceiveTask receiveTask;


    public ThreadClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect(String name, String password) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        socket = new Socket(host, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);

        executor = Executors.newSingleThreadExecutor();
        LoginTask loginTask = new LoginTask(input);
        Future<Boolean> future = executor.submit(loginTask);
        if (password != null) send("_serv_login_" + name + ";" + password);
        else send("_serv_guest_" + name);

        return future.get(3, TimeUnit.SECONDS);
    }

    public void start() {
        receiveTask = new ReceiveTask(input);
        executor.execute(receiveTask);
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
