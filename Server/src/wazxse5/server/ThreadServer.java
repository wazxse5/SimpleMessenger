package wazxse5.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadServer {
    private int port;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private List<Client> clients;

    public ThreadServer(int port) {
        this.port = port;
        loadClients("aa");
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            AcceptingTask acceptingTask = new AcceptingTask(serverSocket);
            acceptingTask.valueProperty().addListener((observable, oldValue, newValue) -> checkNewConnection(newValue));
            executor.execute(acceptingTask);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkNewConnection(Socket socket) {
        Connection connection = new Connection(socket);
        AuthenticationTask authenticationTask = new AuthenticationTask(this, connection);
        Future<Client> result = executor.submit(authenticationTask);
        try {
            Client client = result.get();
            if (client != null) {
                sendFromServer(client, "loginok");
                System.out.println("connected " + client.getName());
            } else connection.close();
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFromServer(Client c, String message) {
        c.getConnection().getOutput().println("_serv_loginok");
    }

    public void loadClients(String path) {
        clients = new ArrayList<>();
        clients.add(new Client("marek", "debil"));
        clients.add(new Client("wazxse5", "dupa"));
    }

    public synchronized Client findClient(String name) {
        for (Client client : clients) {
            if (client.getName().equals(name)) return client;
        }
        return null;
    }
}
