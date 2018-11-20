package wazxse5.server;

import exception.AuthenticationException;
import exception.NameIsInUseException;
import exception.NoSuchUserException;
import exception.WrongPasswordException;

import java.util.ArrayList;
import java.util.List;

public class ClientsLoader {
    private List<Client> clients;

    public ClientsLoader() {
        clients = new ArrayList<>();
        load();
    }

    public void load() {
        clients.add(new Client("test", "test1", false));
        clients.add(new Client("wazxse5", "1234", false));
    }

    public synchronized Client register(String name, String password) throws AuthenticationException {
        for (Client c : clients) {
            if (c.getName().equals(name)) {
                throw new NameIsInUseException();
            }
        }
        Client client = new Client(name, password, false);
        clients.add(client);
        return client;
    }

    public synchronized Client login(String name, String password, boolean isGuest) throws AuthenticationException {
        Client client;
        if (isGuest) client = loginAsGuest(name);
        else client = loginAsUser(name, password);
        return client;
    }

    private Client loginAsGuest(String name) throws NameIsInUseException {
        for (Client c : clients) {
            if (c.getName().equals(name)) {
                throw new NameIsInUseException();
            }
        }
        Client client = new Client(name, null, true);
        clients.add(client);
        return client;
    }

    private Client loginAsUser(String name, String password) throws WrongPasswordException, NoSuchUserException {
        for (Client c : clients) {
            if (c.getName().equals(name)) {
                // TODO: Dodać sprawdzanie czy klient nie został już zaologowany
                if (c.checkPassword(password)) {
                    return c;
                } else throw new WrongPasswordException();
            }
        }
        throw new NoSuchUserException();
    }
}
