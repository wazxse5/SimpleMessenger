package wazxse5.server;

import wazxse5.common.UserInfo;

import java.sql.Connection;
import java.sql.*;

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

    public String loginUser(String login, byte[] password) throws SQLException {
        PreparedStatement loginStatement = connection.prepareStatement("CALL login_user(?,?);");
        loginStatement.setString(1, login);
        loginStatement.setBytes(2, password);
        resultSet = loginStatement.executeQuery();
        resultSet.next();
        String result = resultSet.getString("result");
        int attemptCounter = resultSet.getInt("attempt_counter");
        return attemptCounter + result;
    }

    public boolean isUserNameAvailable(String login) throws SQLException {
        PreparedStatement checkAvailableStatement = connection.prepareStatement("CALL check_if_user_exists(?);");
        checkAvailableStatement.setString(1, login);
        resultSet = checkAvailableStatement.executeQuery();
        resultSet.next();
        int result = resultSet.getInt(1);
        return result == 0;
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
