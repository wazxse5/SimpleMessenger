package wazxse5.server.tasks;

import wazxse5.server.Client;
import wazxse5.server.ClientsLoader;
import wazxse5.server.Connection;

import java.util.concurrent.Callable;

public class AuthenticationTask implements Callable<Client> {
    private Connection connection;
    private ClientsLoader clientsLoader;

    public AuthenticationTask(ClientsLoader clientsLoader, Connection connection) {
        this.connection = connection;
        this.clientsLoader = clientsLoader;
    }

    @Override public Client call() {
        String entryline = connection.getInputLine();
        if (entryline.substring(0, 12).equals("_serv_login_")) {
            entryline = entryline.substring(12);
            int index = entryline.indexOf(';');
            String name = entryline.substring(0, index);
            String password = entryline.substring(index + 1);

            if (clientsLoader.checkName(name)) {
                Client client = clientsLoader.authenticate(name, password);
                if (client != null) {
                    connection.send("_serv", "login_ok");
                    return client;
                } else connection.send("_serv", "login_wrong_password");
            } else connection.send("_serv", "login_no_such_user");
        } else if (entryline.substring(0, 12).equals("_serv_guest_")) {
            String login = entryline.substring(12);
            if (!clientsLoader.checkName(login)) {
                connection.send("_serv", "login_ok");
                return new Client(login, null, true);
            } else connection.send("_serv", "login_name_is_in_use");
        }
        return null;
    }
}
