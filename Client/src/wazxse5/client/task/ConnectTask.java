package wazxse5.client.task;

import javafx.concurrent.Task;
import wazxse5.client.Connection;
import wazxse5.common.message.config.WelcomeMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectTask extends Task<Connection> {
    private String address;
    private int port;

    public ConnectTask(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override public Connection call() throws Exception {
        Socket socket = new Socket(address, port);
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

        WelcomeMessage welcomeMessage = new WelcomeMessage();
        output.writeObject(welcomeMessage);

        while (true) {
            Object answerObject = input.readObject();
            if (answerObject instanceof WelcomeMessage) {
                WelcomeMessage answerMessage = (WelcomeMessage) answerObject;
                if (welcomeMessage.getId() == answerMessage.getId()) {
                    return new Connection(socket, input, output);
                }
            }
            if (Thread.interrupted()) return null;
        }
    }
}
