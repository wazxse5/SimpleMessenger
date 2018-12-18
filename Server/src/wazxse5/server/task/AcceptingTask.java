package wazxse5.server.task;

import javafx.concurrent.Task;
import wazxse5.server.Connection;
import wazxse5.server.DataLoader;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AcceptingTask extends Task<Connection> {
    private final ServerSocket serverSocket;
    private final DataLoader dataLoader;
    private final ExecutorService executor;


    public AcceptingTask(ServerSocket serverSocket, DataLoader dataLoader) {
        this.serverSocket = serverSocket;
        this.dataLoader = dataLoader;
        this.executor = Executors.newCachedThreadPool();
    }

    @Override protected Connection call() throws Exception {
        serverSocket.setSoTimeout(100);
        while (!isCancelled()) {
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket);
                updateValue(connection);
            } catch (SocketTimeoutException ignored) {
            }
        }

        executor.shutdownNow();
        serverSocket.close();
        executor.shutdown();
        return null;
    }

}
