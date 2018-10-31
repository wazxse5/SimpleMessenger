package wazxse5.server;

public class Client {
    private String name;
    private String password;
    private boolean guest;
    private boolean connected;
    private Connection connection;

    public Client(String name, String password, boolean guest) {
        this.name = name;
        this.password = password;
        this.guest = guest;
    }

    public void send(String from, String message) {

    }

    public String getName() {
        return name;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isGuest() {
        return guest;
    }
}
