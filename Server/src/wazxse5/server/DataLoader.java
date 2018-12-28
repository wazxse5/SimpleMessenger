package wazxse5.server;

import wazxse5.common.UserInfo;
import wazxse5.common.exception.AuthenticationException;
import wazxse5.common.exception.LoginIsNotAvailableException;

import java.sql.SQLException;
import java.util.List;

public class DataLoader {
    private MysqlConnector mysqlConnector;
    private List<User> knownUsers;
    private List<User> guests;

    public DataLoader() {
        try {
            mysqlConnector = new MysqlConnector();
            mysqlConnector.connect("localhost", "messenger", "root", "");
        } catch (Exception ignored) {
        } // TODO: 17.12.2018 Dopisać obsługę błędu połączenia z bazą
    }

    public synchronized boolean register(UserInfo userInfo, byte[] password) throws SQLException, LoginIsNotAvailableException {
        String result = mysqlConnector.registerUser(userInfo, password);
        if (result.equals("ok")) return true;
        else if (result.equals("login_not_available")) throw new LoginIsNotAvailableException();
        else return false;
    }

    public synchronized User login(String userLogin, byte[] password, boolean isGuest) throws AuthenticationException, SQLException {
        if (isGuest) {
            boolean result = mysqlConnector.loginGuest(userLogin);
            if (result) return findUser(userLogin);
        } else {
            return mysqlConnector.loginUser(userLogin, password);
        }
        return null;
    }

    private User findUser(String userLogin) {
        for (User user : knownUsers) {
            if (user.getUserInfo().getLogin().equals(userLogin)) return user;
        }
        return null;
    }

}
