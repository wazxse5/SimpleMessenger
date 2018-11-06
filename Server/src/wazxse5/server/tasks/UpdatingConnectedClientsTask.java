package wazxse5.server.tasks;

import wazxse5.server.Client;
import wazxse5.server.ThreadServer;

import java.util.List;

public class UpdatingConnectedClientsTask implements Runnable {
    private ThreadServer threadServer;

    public UpdatingConnectedClientsTask(ThreadServer threadServer) {
        this.threadServer = threadServer;
    }

    @Override public void run() {
        while (!Thread.interrupted()) {
            List<Client> connectedClients = threadServer.getConnectedClients();
            for (int i = 0; i < connectedClients.size(); i++) {
                for (int j = 0; j < connectedClients.size(); j++) {
                    if (i != j) {
                        connectedClients.get(i).send("_serv", "connected_" + connectedClients.get(j).getName());
                    }
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
