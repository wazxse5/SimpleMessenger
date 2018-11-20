package wazxse5.server;

import javafx.concurrent.Task;
import message.Message;
import org.omg.CORBA.Object;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection extends Task {
    private final Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    public Connection(Socket socket) {
        this.socket = socket;
        try {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override protected Object call() {
        return null;
    }

    public void send(Message message) throws IOException {
        outputStream.writeObject(message);
    }

    public void close() throws IOException {
        socket.close();
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }
}
