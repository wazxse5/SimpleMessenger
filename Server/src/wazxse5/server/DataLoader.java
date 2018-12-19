package wazxse5.server;

import wazxse5.common.UserInfo;
import wazxse5.common.exception.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private MysqlConnector mysqlConnector;
    private List<User> knownUsers;
    private List<User> guests;

    public DataLoader() {
        knownUsers = new ArrayList<>();
        try {
            mysqlConnector = new MysqlConnector();
            mysqlConnector.connect("localhost", "messenger", "root", "");
        } catch (Exception ignored) {
        } // TODO: 17.12.2018 Dopisać obsługę błędu połączenia z bazą
    }

    public synchronized void register(UserInfo userInfo, byte[] password) {
        try {
            String a = mysqlConnector.registerUser(userInfo, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized User login(String userName, byte[] password, boolean isGuest) throws AuthenticationException, SQLException {
        if (isGuest) {
            if (mysqlConnector.isUserNameAvailable(userName)) {
                User guest = null; // FIXME: 18.12.2018
                guests.add(guest);
            } else throw new LoginIsNotAvailableException();
        } else {
            String result = mysqlConnector.loginUser(userName, password);
            int passwordAttempts = Character.getNumericValue(result.indexOf(0));
            result = result.substring(1);
            if (result.equals("ok")) {
                return new User();
            } else if (result.equals("no_attempt")) throw new NoPasswordAttemptsException();
            else if (result.equals("wrong_pass")) throw new WrongPasswordException(passwordAttempts);
            else if (result.equals("no_user")) throw new LoginNotExistsException();
        }
        return null;
    }

}
