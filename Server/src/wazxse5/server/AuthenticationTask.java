package wazxse5.server;

import java.util.concurrent.Callable;

public class AuthenticationTask implements Callable<Client> {
    private ThreadServer threadServer;
    private Connection connection;

    public AuthenticationTask(ThreadServer threadServer, Connection connection) {
        this.threadServer = threadServer;
        this.connection = connection;
    }

    @Override public Client call() {
        String line = connection.getInputLine();
        if (line.substring(0, 12).equals("_serv_login_")) {
            line = line.substring(12);
            int index = line.indexOf(';');
            String login = line.substring(0, index);
            String pass = line.substring(index + 1);

            Client client = threadServer.findClient(login);
            if (client != null) {
                if (client.checkPassword(pass)) {
                    client.setConnected(true);
                    client.setConnection(connection);
                    connection.sendServer("login_ok");
                    return client;
                } else connection.sendServer("login_wrong_password");
            } else connection.sendServer("login_no_such_user");
        } else if (line.substring(0, 12).equals("_serv_guest_")) {
            String login = line.substring(12);
            if (threadServer.findClient(login) == null) {
                Client client = new Client(login, null, true);
                client.setConnected(true);
                client.setConnection(connection);
                connection.sendServer("login_ok");
                return client;
            } else connection.sendServer("login_name_is_in_use");
        }
        return null;
    }
}
