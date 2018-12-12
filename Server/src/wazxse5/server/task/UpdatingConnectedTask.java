package wazxse5.server.task;

import wazxse5.common.message.config.SessionMessage;
import wazxse5.server.ThreadServer;
import wazxse5.server.User;

import java.util.ArrayList;
import java.util.List;

public class UpdatingConnectedTask implements Runnable {
    private ThreadServer threadServer;

    public UpdatingConnectedTask(ThreadServer threadServer) {
        this.threadServer = threadServer;
    }

    @Override public void run() {
        while (!Thread.interrupted()) {
            List<User> connectedUsers = threadServer.getConnectedUsers();
            List<String> connectedClientsNames = new ArrayList<>();
            for (User c : connectedUsers) connectedClientsNames.add(c.getName());

            for (User connectedUser : connectedUsers) {
                connectedUser.send(new SessionMessage(1, connectedClientsNames));
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
