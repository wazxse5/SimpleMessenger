package wazxse5.server;

import wazxse5.common.UserInfo;
import wazxse5.common.exception.*;

import java.sql.Connection;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MysqlConnector {
    private Connection connection;
    private PreparedStatement lgUsSt;
    private PreparedStatement lgGuSt;
    private PreparedStatement rgUsSt;

    public MysqlConnector() throws ClassNotFoundException {
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
    }

    public void connect(String dbAddress, String dbName, String dbUserName, String dbPassword) throws SQLException {
        String dbUrl = "jdbc:mysql://" + dbAddress + "/" + dbName + "?characterEncoding=utf8";
        connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        lgUsSt = connection.prepareStatement("CALL login_user(?,?);");
        lgGuSt = connection.prepareStatement("CALL login_guest(?);");
        rgUsSt = connection.prepareStatement("CALL register_user(?, ? ,?, ?, ?);");
    }

    public synchronized User loginUser(String userLogin, String password) throws SQLException, AuthenticationException {
        lgUsSt.setString(1, userLogin);
        lgUsSt.setString(2, password);
        ResultSet resultSet = lgUsSt.executeQuery();
        resultSet.next();
        String result = resultSet.getString(1);

        if (result.equals("ok")) {
            int userID = resultSet.getInt("id_user");
            String userMail = resultSet.getString("mail");
            String userName = resultSet.getString("name");
            String userSurname = resultSet.getString("surname");
            LocalDateTime userRegistrationTime = LocalDateTime.ofInstant(resultSet.getTimestamp("registration_time").toInstant(), ZoneId.systemDefault());
            UserInfo userInfo = new UserInfo(userID, userName, userSurname, userMail, userLogin, userRegistrationTime, 3, false);
            return new User(userInfo);
        } else if (result.equals("no_attempts")) throw new NoPasswordAttemptsException();
        else if (result.equals("wrong_password")) throw new WrongPasswordException(resultSet.getInt(2));
        else if (result.equals("no_user")) throw new LoginNotExistsException();
        return null;
    }

    public synchronized User loginGuest(String userLogin) throws SQLException, LoginIsNotAvailableException {
        lgGuSt.setString(1, userLogin);
        ResultSet resultSet = lgGuSt.executeQuery();
        resultSet.next();
        String result = resultSet.getString(1);

        if (result.equals("ok")) return User.createGuest(userLogin);
        else if (result.equals("login_not_available")) throw new LoginIsNotAvailableException();
        else return null;
    }

    public synchronized String registerUser(UserInfo userInfo, String password) throws SQLException {
        rgUsSt.setString(1, userInfo.getLogin());
        rgUsSt.setString(2, password);
        rgUsSt.setString(3, userInfo.getMail());
        rgUsSt.setString(4, userInfo.getName());
        rgUsSt.setString(5, userInfo.getSurname());
        ResultSet resultSet = rgUsSt.executeQuery();
        resultSet.next();
        return resultSet.getString("result");
    }

}
