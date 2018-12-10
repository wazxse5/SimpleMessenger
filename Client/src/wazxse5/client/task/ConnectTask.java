package wazxse5.client.task;

import exception.AuthenticationException;
import exception.NameIsInUseException;
import exception.NoSuchUserException;
import exception.WrongPasswordException;
import javafx.concurrent.Task;
import message.config.LoginAnswerMessage;
import message.config.LoginRequestMessage;
import wazxse5.client.Connection;
import wazxse5.client.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectTask extends Task<Connection> {
    private String address;
    private int port;
    private String login;
    private String password;
    private boolean guest;

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ConnectTask(String address, int port, String login, String password, boolean guest) {
        this.address = address;
        this.port = port;
        this.login = login;
        this.password = password;
        this.guest = guest;
    }

    @Override public Connection call() throws Exception {
        try {
            socket = new Socket(address, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            output.writeObject(new LoginRequestMessage(login, password, guest));

            while (true) {
                Object answerObject = input.readObject();
                if (answerObject instanceof LoginAnswerMessage) {
                    LoginAnswerMessage loginAnswerMessage = (LoginAnswerMessage) answerObject;
                    return handleLoginAnswer(loginAnswerMessage);
                }
                if (Thread.interrupted()) return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Connection handleLoginAnswer(LoginAnswerMessage loginAnswerMessage) throws AuthenticationException {
        if (loginAnswerMessage.isGood()) {
            User user = new User(loginAnswerMessage.getName(), loginAnswerMessage.getSurname(), loginAnswerMessage.getMail(), loginAnswerMessage.getLogin());
            System.out.println("dupa");
            return new Connection(user, socket, input, output);
        } else {
            if (loginAnswerMessage.getInfoCode() == 1) throw new NameIsInUseException();
            else if (loginAnswerMessage.getInfoCode() == 2) throw new NoSuchUserException();
            else if (loginAnswerMessage.getInfoCode() == 3) throw new WrongPasswordException();
            return null;
        }
    }
}
