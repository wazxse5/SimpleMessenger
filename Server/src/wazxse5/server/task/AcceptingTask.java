package wazxse5.server.task;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import wazxse5.server.Connection;
import wazxse5.server.DataLoader;
import wazxse5.server.User;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AcceptingTask extends Task<User> {
    private final ServerSocket serverSocket;
    private final DataLoader dataLoader;
    private final ExecutorService executor;


    public AcceptingTask(ServerSocket serverSocket, DataLoader dataLoader) {
        this.serverSocket = serverSocket;
        this.dataLoader = dataLoader;
        this.executor = Executors.newCachedThreadPool();
    }

    @Override protected User call() throws Exception {
        serverSocket.setSoTimeout(100);
        while (!isCancelled()) {
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket);
                AuthenticationTask authenticationTask = new AuthenticationTask(dataLoader, connection);
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
        User user = (User) authenticationFinished.getSource().getValue();
        if (user != null) {
            updateValue(user);
        }
    }

}
