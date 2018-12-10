package wazxse5.server.task;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import wazxse5.server.Client;
import wazxse5.server.ClientsLoader;
import wazxse5.server.Connection;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AcceptingTask extends Task<Client> {
    private final ServerSocket serverSocket;
    private final ClientsLoader clientsLoader;
    private final ExecutorService executor;


    public AcceptingTask(ServerSocket serverSocket, ClientsLoader clientsLoader) {
        this.serverSocket = serverSocket;
        this.clientsLoader = clientsLoader;
        this.executor = Executors.newCachedThreadPool();
    }

    @Override protected Client call() throws Exception {
        serverSocket.setSoTimeout(100);
        while (!isCancelled()) {
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket);
                AuthenticationTask authenticationTask = new AuthenticationTask(clientsLoader, connection);
                authenticationTask.setOnSucceeded(this::returnConnectedClient);
                executor.submit(authenticationTask);
            } catch (SocketTimeoutException ignored) {
            }
        }

        executor.shutdownNow();
        serverSocket.close();
        executor.shutdown();
        return null;
    }

    private void returnConnectedClient(WorkerStateEvent authenticationFinished) {
        Client client = (Client) authenticationFinished.getSource().getValue();
        if (client != null) {
            updateValue(client);
        }
    }

}
