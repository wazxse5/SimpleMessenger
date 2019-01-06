package wazxse5.server;

import wazxse5.common.UserInfo;
import wazxse5.common.exception.*;

import java.sql.Connection;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MysqlConnector {
    private Connection connection;
    private ResultSet resultSet;

    public MysqlConnector() throws ClassNotFoundException {
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
    }

    public void connect(String address, String dbName, String dbUserName, String dbPassword) throws SQLException {
        String dbUrl = "jdbc:mysql://" + address + "/" + dbName + "?characterEncoding=utf8";
        connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
    }

    public User loginUser(String userLogin, byte[] password) throws SQLException, AuthenticationException {
        PreparedStatement loginStatement = connection.prepareStatement("CALL login_user(?,?);");
        loginStatement.setString(1, userLogin);
        loginStatement.setBytes(2, password);

        resultSet = loginStatement.executeQuery();
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

    public User loginGuest(String userLogin) throws SQLException, LoginIsNotAvailableException {
        PreparedStatement loginStatement = connection.prepareStatement("CALL login_guest(?);");
        loginStatement.setString(1, userLogin);

        resultSet = loginStatement.executeQuery();
        resultSet.next();
        String result = resultSet.getString(1);

        if (result.equals("ok")) return User.createGuest(userLogin);
        else if (result.equals("login_not_available")) throw new LoginIsNotAvailableException();
        else return null;
    }

    public String registerUser(UserInfo userInfo, byte[] password) throws SQLException {
        PreparedStatement registerStatement = connection.prepareStatement("CALL create_user(?, ? ,?, ?, ?);");
        registerStatement.setString(1, userInfo.getLogin());
        registerStatement.setBytes(2, password);
        registerStatement.setString(3, userInfo.getMail());
        registerStatement.setString(4, userInfo.getName());
        registerStatement.setString(5, userInfo.getSurname());
        resultSet = registerStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("result");
    }

}
