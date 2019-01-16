package wazxse5.server;

import javafx.collections.ObservableList;
import wazxse5.common.UserInfo;
import wazxse5.common.exception.AuthenticationException;
import wazxse5.common.exception.LoginIsNotAvailableException;
import wazxse5.common.exception.UserIsAlreadyLoggedException;

import java.sql.SQLException;

public class DataLoader {
    private MysqlConnector mysqlConnector;
    private final ObservableList<String> loggedUsersLogins;

    public DataLoader(ObservableList<String> loggedUsersLogins) {
        this.loggedUsersLogins = loggedUsersLogins;
    }

    public synchronized boolean register(UserInfo userInfo, String password) throws SQLException, LoginIsNotAvailableException {
        String result = mysqlConnector.registerUser(userInfo, password);
        if (result.equals("ok")) return true;
        else if (result.equals("login_not_available")) throw new LoginIsNotAvailableException();
        else return false;
    }

    public synchronized User login(String userLogin, String password, boolean isGuest) throws AuthenticationException, SQLException {
        User user;
        if (isGuest) {
            if (loggedUsersLogins.contains(userLogin)) throw new LoginIsNotAvailableException();
            user = mysqlConnector.loginGuest(userLogin);
        } else {
            if (loggedUsersLogins.contains(userLogin)) throw new UserIsAlreadyLoggedException();
            else user = mysqlConnector.loginUser(userLogin, password);
        }
        return user;
    }

    public void setMysqlConnector(MysqlConnector mysqlConnector) {
        this.mysqlConnector = mysqlConnector;
    }
}
