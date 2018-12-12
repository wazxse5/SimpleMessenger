package wazxse5.server.task;

import exception.AuthenticationException;
import javafx.concurrent.Task;
import message.config.LoginAnswerMessage;
import message.config.LoginRequestMessage;
import message.config.RegisterAnswerMessage;
import message.config.RegisterRequestMessage;
import wazxse5.server.Connection;
import wazxse5.server.DataLoader;
import wazxse5.server.User;

import java.io.IOException;
import java.io.ObjectInputStream;

public class AuthenticationTask extends Task<User> {
    private final Connection connection;
    private final DataLoader dataLoader;

    public AuthenticationTask(DataLoader dataLoader, Connection connection) {
        this.connection = connection;
        this.dataLoader = dataLoader;
    }


    @Override protected User call() {
        User user = null;
        try {
            ObjectInputStream input = connection.getInputStream();

            Object authenticationObject = input.readObject();
            if (authenticationObject instanceof LoginRequestMessage) {
                user = login(authenticationObject);
            } else if (authenticationObject instanceof RegisterRequestMessage) {
                user = register(authenticationObject);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    private User login(Object authenticationObject) throws IOException {
        LoginRequestMessage loginRequest = (LoginRequestMessage) authenticationObject;
        String name = loginRequest.getName();
        String password = loginRequest.getPassword();
        boolean isGuest = loginRequest.isGuest();
        try {
            User user = dataLoader.login(name, password, isGuest);
            connection.send(new LoginAnswerMessage(true));
            user.setConnected(true);
            user.setConnection(connection);
            return user;
        } catch (AuthenticationException e) {
            connection.send(new LoginAnswerMessage(false, e.getCode()));
        }
        return null;
    }

    private User register(Object authenticationObject) throws IOException {
        RegisterRequestMessage registerRequest = (RegisterRequestMessage) authenticationObject;
        String name = registerRequest.getName();
        String password = registerRequest.getPassword();
        try {
            User user = dataLoader.register(name, password);
            connection.send(new RegisterAnswerMessage(true));
            return user;
        } catch (AuthenticationException e) {
            connection.send(new RegisterAnswerMessage(false, e.getCode()));
        }
        return null;
    }

}
