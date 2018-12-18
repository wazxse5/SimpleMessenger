package wazxse5.server;

import wazxse5.common.UserInfo;

import java.sql.Connection;
import java.sql.*;

public class MysqlConnector {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public MysqlConnector() throws ClassNotFoundException {
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
    }

    public void connect(String address, String dbName, String dbUserName, String dbPassword) throws SQLException {
        String dbUrl = "jdbc:mysql://" + address + "/" + dbName + "?characterEncoding=utf8";
        connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        statement = connection.createStatement();
    }

    public String loginUser(String login, byte[] password) throws SQLException {
        String query = "CALL login_user('" + login + "','" + password + "');";
        resultSet = statement.executeQuery(query);
        resultSet.next();
        String result = resultSet.getString("result");
        int attemptCounter = resultSet.getInt("attempt_counter");
        return attemptCounter + result;
    }

    public boolean isUserNameAvailable(String login) throws SQLException {
        String query = "CALL check_if_user_exists('" + login + "');";
        resultSet = statement.executeQuery(query);
        resultSet.next();
        int result = resultSet.getInt(1);
        return result == 0;
    }

    public String registerUser(UserInfo userInfo, byte[] password) throws SQLException {
        String query = "CALL create_user('" + userInfo.getLogin() +
                "','" + password +
                "','" + userInfo.getMail() +
                "','" + userInfo.getName() +
                "','" + userInfo.getSurname() + "');";
        resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getString("result");
    }

}
