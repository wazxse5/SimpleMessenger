package wazxse5.server.task;

import javafx.concurrent.Task;
import wazxse5.common.UserInfo;
import wazxse5.common.exception.DatabaseException;
import wazxse5.common.exception.LoginIsNotAvailableException;
import wazxse5.common.message.config.RegisterAnswerMessage;
import wazxse5.common.message.config.RegisterRequestMessage;
import wazxse5.server.Connection;
import wazxse5.server.DataLoader;

import java.sql.SQLException;

public class RegisterTask extends Task<Void> {
    private final Connection connection;
    private DataLoader dataLoader;
    private RegisterRequestMessage registerRequestMessage;

    public RegisterTask(DataLoader dataLoader, Connection connection, RegisterRequestMessage registerRequestMessage) {
        this.connection = connection;
        this.dataLoader = dataLoader;
        this.registerRequestMessage = registerRequestMessage;
    }

    @Override protected Void call() {
        UserInfo userInfo = registerRequestMessage.getUserInfo();
        String password = registerRequestMessage.getPassword();
        try {
            if (dataLoader.register(userInfo, password)) connection.send(new RegisterAnswerMessage(true, null));
            else throw new SQLException();
        } catch (SQLException e) {
            connection.send(new RegisterAnswerMessage(false, new DatabaseException()));
        } catch (LoginIsNotAvailableException e) {
            connection.send(new RegisterAnswerMessage(false, e));
        }

        return null;
    }
}
