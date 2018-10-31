package wazxse5.server;

import java.util.concurrent.Callable;

public class AuthenticationTask implements Callable<Client> {
    private ThreadServer threadServer;
    private Connection connection;

    public AuthenticationTask(ThreadServer threadServer, Connection connection) {
        this.threadServer = threadServer;
        this.connection = connection;
    }

    @Override public Client call() throws Exception {
        String line = connection.getInputLine();
        line = line.substring(6);
        int index = line.indexOf(';');
        String login = line.substring(0, index);
        String pass = line.substring(index + 1);

        Client client = threadServer.findClient(login);
        if (client != null) {
            if (client.checkPassword(pass)) {
                client.setConnected(true);
                client.setConnection(connection);
                return client;
            }
        }
        return null;
    }
}
