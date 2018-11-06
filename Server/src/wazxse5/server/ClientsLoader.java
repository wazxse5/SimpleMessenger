package wazxse5.server;

import java.util.ArrayList;
import java.util.List;

public class ClientsLoader {
    private List<Client> clients;
    private Client previouslyCheckedClient;

    public ClientsLoader() {
        clients = new ArrayList<>();
        load();
    }

    public void load() {
        clients.add(new Client("test", "test1", false));
        clients.add(new Client("wazxse5", "1234", false));
    }

    public synchronized boolean checkName(String name) {
        for (Client client : clients) {
            if (client.getName().equals(name)) {
                previouslyCheckedClient = client;
                return true;
            }
        }
        return false;
    }

    public synchronized Client authenticate(String name, String password) {
        if (previouslyCheckedClient != null && previouslyCheckedClient.getName().equals(name)) {
            if (previouslyCheckedClient.checkPassword(password)) return previouslyCheckedClient;
        } else {
            for (Client client : clients) {
                if (client.getName().equals(name)) {
                    if (client.checkPassword(password)) return client;
                }
            }
        }
        return null;
    }
}
