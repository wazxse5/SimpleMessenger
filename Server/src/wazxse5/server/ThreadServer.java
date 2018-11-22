package wazxse5.server;

import message.Message;
import message.ServerMessage;
import message.UserMessage;
import message.config.GoodbyeMessage;
import wazxse5.server.task.AcceptingTask;
import wazxse5.server.task.ReceiveTask;
import wazxse5.server.task.UpdatingConnectedClientsTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadServer {
    private final int port;

    private final ExecutorService executor;
    private Future acceptingTaskFuture;
    private AcceptingTask acceptingTask;
    private Future updatingConnectedClientsFuture;

    private final ClientsLoader clientsLoader;
    private final List<Client> connectedClients;

    public ThreadServer(int port) {
        this.port = port;

        executor = Executors.newCachedThreadPool();

        clientsLoader = new ClientsLoader();
        connectedClients = new ArrayList<>();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            acceptingTask = new AcceptingTask(serverSocket, clientsLoader);
            acceptingTask.valueProperty().addListener((observable, oldValue, newValue) -> addNewConnectedClient(newValue));
            acceptingTaskFuture = executor.submit(acceptingTask);

            UpdatingConnectedClientsTask updatingConnectedClientsTask = new UpdatingConnectedClientsTask(this);
            updatingConnectedClientsFuture = executor.submit(updatingConnectedClientsTask);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewConnectedClient(Client client) {
        connectedClients.add(client);
        ReceiveTask receiveTask = new ReceiveTask(client.getConnection().getInputStream());
        receiveTask.valueProperty().addListener((observable, oldValue, newValue) -> handleReceivedMessage(client, newValue));
        executor.submit(receiveTask);
    }

    private void handleReceivedMessage(Client client, Message message) {
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage) message;
            client.handleReceivedMessage(userMessage);
        } else if (message instanceof ServerMessage) {
            if (message instanceof GoodbyeMessage) {
                client.setConnected(false);
                connectedClients.remove(client);
                try {
                    client.getConnection().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Client> getConnectedClients() {
        return connectedClients;
    }

    public void close() {
        executor.shutdown();
        updatingConnectedClientsFuture.cancel(true);

        acceptingTaskFuture.cancel(true);
        acceptingTask.cancel(true);
    }
}
