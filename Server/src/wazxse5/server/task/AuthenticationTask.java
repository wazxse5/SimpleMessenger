package wazxse5.server.task;

import exception.AuthenticationException;
import javafx.concurrent.Task;
import message.config.LoginAnswerMessage;
import message.config.LoginRequestMessage;
import message.config.RegisterAnswerMessage;
import message.config.RegisterRequestMessage;
import wazxse5.server.Client;
import wazxse5.server.ClientsLoader;
import wazxse5.server.Connection;

import java.io.IOException;
import java.io.ObjectInputStream;

public class AuthenticationTask extends Task<Client> {
    private final Connection connection;
    private final ClientsLoader clientsLoader;

    public AuthenticationTask(ClientsLoader clientsLoader, Connection connection) {
        this.connection = connection;
        this.clientsLoader = clientsLoader;
    }


    @Override protected Client call() {
        Client client = null;
        try {
            ObjectInputStream input = connection.getInputStream();

            Object authenticationObject = input.readObject();
            if (authenticationObject instanceof LoginRequestMessage) {
                client = login(authenticationObject);
            } else if (authenticationObject instanceof RegisterRequestMessage) {
                client = register(authenticationObject);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return client;
    }

    private Client login(Object authenticationObject) throws IOException {
        LoginRequestMessage loginRequest = (LoginRequestMessage) authenticationObject;
        String name = loginRequest.getName();
        String password = loginRequest.getPassword();
        boolean isGuest = loginRequest.isGuest();
        try {
            Client client = clientsLoader.login(name, password, isGuest);
            connection.send(new LoginAnswerMessage(true));
            client.setConnected(true);
            client.setConnection(connection);
            return client;
        } catch (AuthenticationException e) {
            connection.send(new LoginAnswerMessage(false, e.getCode()));
        }
        return null;
    }

    private Client register(Object authenticationObject) throws IOException {
        RegisterRequestMessage registerRequest = (RegisterRequestMessage) authenticationObject;
        String name = registerRequest.getName();
        String password = registerRequest.getPassword();
        try {
            Client client = clientsLoader.register(name, password);
            connection.send(new RegisterAnswerMessage(true));
            return client;
        } catch (AuthenticationException e) {
            connection.send(new RegisterAnswerMessage(false, e.getCode()));
        }
        return null;
    }

}
