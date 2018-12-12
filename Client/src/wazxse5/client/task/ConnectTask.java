package wazxse5.client.task;

import javafx.concurrent.Task;
import wazxse5.client.Connection;
import wazxse5.common.UserInfo;
import wazxse5.common.exception.AuthenticationException;
import wazxse5.common.exception.LoginIsInUseException;
import wazxse5.common.exception.NoSuchUserException;
import wazxse5.common.exception.WrongPasswordException;
import wazxse5.common.message.config.LoginAnswerMessage;
import wazxse5.common.message.config.LoginRequestMessage;

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
    }

    private Connection handleLoginAnswer(LoginAnswerMessage loginAnswerMessage) throws AuthenticationException {
        if (loginAnswerMessage.isGood()) {
            UserInfo userInfo = loginAnswerMessage.getUserInfo();
            return new Connection(userInfo, socket, input, output);
        } else {
            if (loginAnswerMessage.getInfoCode() == 1) throw new LoginIsInUseException();
            else if (loginAnswerMessage.getInfoCode() == 2) throw new NoSuchUserException();
            else if (loginAnswerMessage.getInfoCode() == 3) throw new WrongPasswordException();
            return null;
        }
    }
}
