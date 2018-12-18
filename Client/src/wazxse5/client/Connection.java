package wazxse5.client;

import wazxse5.common.UserInfo;
import wazxse5.common.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
    private final Socket socket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private UserInfo userInfo;

    public Connection(Socket socket, ObjectInputStream input, ObjectOutputStream output) {
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

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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

    public UserInfo getUserInfo() {
        return userInfo;
    }
}