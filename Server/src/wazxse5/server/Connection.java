package wazxse5.server;

import javafx.beans.property.*;
import wazxse5.common.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
    private ViewManager viewManager;
    private static int idCounter = 0;
    private StringProperty id = new SimpleStringProperty();

    private final Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private BooleanProperty logged = new SimpleBooleanProperty(false);
    private ObjectProperty<User> user = new SimpleObjectProperty<>();


    public Connection(Socket socket) {
        this.id.setValue(Integer.toString(idCounter++));
        this.user.setValue(User.createEmptyUser());
        this.socket = socket;
        try {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void send(Message message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user.get();
    }

    public ObjectProperty<User> userProperty() {
        return user;
    }

    public void close() {
        try {
            if (!socket.isClosed()) socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        this.user.setValue(user);
        viewManager.refreshConnectedConnectionsTable();
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setLogged(boolean logged) {
        this.logged.set(logged);
    }

    public boolean isLogged() {
        return logged.get();
    }
}
