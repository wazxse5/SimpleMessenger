package wazxse5.server;

import javafx.concurrent.Task;

import java.net.ServerSocket;
import java.net.Socket;

public class AcceptingTask extends Task<Socket> {
    private ServerSocket serverSocket;

    public AcceptingTask(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override protected Socket call() throws Exception {
        while (true) {
            if (!isCancelled()) {
                Socket socket = serverSocket.accept();
                updateValue(socket);
            } else break;
        }
        return null;
    }
}
