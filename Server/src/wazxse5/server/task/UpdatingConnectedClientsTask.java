package wazxse5.server.task;

import message.config.SessionMessage;
import wazxse5.server.Client;
import wazxse5.server.ThreadServer;

import java.util.ArrayList;
import java.util.List;

public class UpdatingConnectedClientsTask implements Runnable {
    private ThreadServer threadServer;

    public UpdatingConnectedClientsTask(ThreadServer threadServer) {
        this.threadServer = threadServer;
    }

    @Override public void run() {
        while (!Thread.interrupted()) {
            List<Client> connectedClients = threadServer.getConnectedClients();
            List<String> connectedClientsNames = new ArrayList<>();
            for (Client c : connectedClients) connectedClientsNames.add(c.getName());

            for (Client connectedClient : connectedClients) {
                connectedClient.send(new SessionMessage(1, connectedClientsNames));
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
