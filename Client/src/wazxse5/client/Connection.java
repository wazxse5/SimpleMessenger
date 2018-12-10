package wazxse5.client;

import message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
    private final User user;
    private final Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Connection(User user, Socket socket, ObjectInputStream input, ObjectOutputStream output) {
        this.user = user;
        this.socket = socket;
        this.input = input;
        this.output = output;
    }

    public void send(Message message) {
        try {
            output.writeObject(message);
        } catch (IOException ignored) {
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public String getAddress() {
        return String.valueOf(socket.getInetAddress());
    }

    public int getPort() {
        return socket.getPort();
    }

    public User getUser() {
        return user;
    }
}