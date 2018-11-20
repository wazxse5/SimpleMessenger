package wazxse5.server.task;

import javafx.concurrent.Task;
import wazxse5.server.Client;
import wazxse5.server.ClientsLoader;
import wazxse5.server.Connection;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AcceptingTask extends Task<Client> {
    private final ServerSocket serverSocket;
    private final ClientsLoader clientsLoader;
    private final ExecutorService executor;

    private final Map<Future<Client>, Connection> authenticationTasks;


    public AcceptingTask(ServerSocket serverSocket, ClientsLoader clientsLoader) {
        this.serverSocket = serverSocket;
        this.clientsLoader = clientsLoader;
        this.executor = Executors.newCachedThreadPool();
        authenticationTasks = new LinkedHashMap<>();
    }

    @Override protected Client call() throws Exception {
        serverSocket.setSoTimeout(100);
        while (!isCancelled()) {
            try {
                checkDoneAuthenticationsTasks();
                try {
                    Socket socket = serverSocket.accept();
                    Connection connection = new Connection(socket);
                    AuthenticationTask authenticationTask = new AuthenticationTask(clientsLoader, connection);
                    authenticationTasks.put(executor.submit(authenticationTask), connection);
                } catch (SocketTimeoutException ignored) {
                }
            } catch (InterruptedException e) {
                break;
            }
        }

        for (Connection connection : authenticationTasks.values()) {
            connection.close();
        }
        serverSocket.close();
        executor.shutdown();
        return null;
    }

    private void checkDoneAuthenticationsTasks() throws ExecutionException, InterruptedException {
        for (Future<Client> future : authenticationTasks.keySet()) {
            if (future.isDone()) {
                Client client = future.get();
                if (client != null) {
                    client.setConnected(true);
                    client.setConnection(authenticationTasks.get(future));
                    updateValue(client);
                }
                authenticationTasks.remove(future);
            }
        }
    }
}
