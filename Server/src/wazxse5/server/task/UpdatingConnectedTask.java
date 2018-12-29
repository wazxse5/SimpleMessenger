package wazxse5.server.task;

import javafx.collections.ObservableList;
import wazxse5.common.message.config.SessionMessage;
import wazxse5.server.Connection;

import java.util.ArrayList;

public class UpdatingConnectedTask implements Runnable {
    private ObservableList<Connection> connectedConnections;
    private ObservableList<String> loggedUserNames;

    public UpdatingConnectedTask(ObservableList<Connection> connectedConnections, ObservableList<String> loggedUserNames) {
        this.connectedConnections = connectedConnections;
        this.loggedUserNames = loggedUserNames;
    }

    @Override public void run() {
        while (!Thread.interrupted()) {
            for (Connection connection : connectedConnections) {
                if (connection.isLogged()) connection.send(new SessionMessage(1, new ArrayList<>(loggedUserNames)));
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
