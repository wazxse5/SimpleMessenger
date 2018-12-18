package wazxse5.server.task;

import javafx.concurrent.Task;
import wazxse5.common.exception.AuthenticationException;
import wazxse5.common.message.config.LoginAnswerMessage;
import wazxse5.common.message.config.LoginRequestMessage;
import wazxse5.server.Connection;
import wazxse5.server.DataLoader;
import wazxse5.server.User;

import java.sql.SQLException;

public class LoginTask extends Task<Void> {
    private final Connection connection;
    private DataLoader dataLoader;
    private LoginRequestMessage loginRequestMessage;

    public LoginTask(DataLoader dataLoader, Connection connection, LoginRequestMessage loginRequestMessage) {
        this.connection = connection;
        this.dataLoader = dataLoader;
        this.loginRequestMessage = loginRequestMessage;
    }


    @Override protected Void call() {
        String name = loginRequestMessage.getLogin();
        byte[] password = loginRequestMessage.getPassword();
        boolean isGuest = loginRequestMessage.isGuest();
        try {
            User user = dataLoader.login(name, password, isGuest);
            user.setConnected(true);
            user.setConnection(connection);
            connection.send(new LoginAnswerMessage(true, null, user.getUserInfo()));
        } catch (AuthenticationException exception) {
            connection.send(new LoginAnswerMessage(false, exception, null));
        } catch (SQLException ignored) {
        }
        return null;
    }

}
