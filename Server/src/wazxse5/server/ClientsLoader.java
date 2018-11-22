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
        clients.add(new Client("test", false));
        clients.add(new Client("wazxse5", false));

        for (Client client : clients) for (Client friend : clients) if (client != friend) client.addFriend(friend);
    }

    public synchronized Client register(String name, String password) throws AuthenticationException {
        for (Client c : clients) {
            if (c.getName().equals(name)) {
                throw new NameIsInUseException();
            }
        }
        Client client = new Client(name, false);
        clients.add(client);
        return client;
    }

    public synchronized Client login(String name, String password, boolean isGuest) throws AuthenticationException {
        Client client;
        if (isGuest) client = loginAsGuest(name);
        else client = loginAsUser(name, password);
        return client;
    }

    private synchronized Client loginAsGuest(String name) throws NameIsInUseException {
        for (Client c : clients) {
            if (c.getName().equals(name)) {
                throw new NameIsInUseException();
            }
        }
        Client client = new Client(name, true, clients);
        clients.add(client);
        return client;
    }

    private Client loginAsUser(String name, String password) throws WrongPasswordException, NoSuchUserException {
        for (Client c : clients) {
            if (c.getName().equals(name)) {
                // TODO: Dodać sprawdzanie czy klient nie został już zaologowany
                if (checkPassword(c, password)) {
                    return c;
                } else throw new WrongPasswordException();
            }
        }
        throw new NoSuchUserException();
    }

    private boolean checkPassword(Client client, String password) {
        // TODO: sprawdzanie hasła z pliku
        if (client.getName().equals("wazxse5") && password.equals("1234")) return true;
        else if (client.getName().equals("test") && password.equals("0000")) return true;
        else return false;
    }


}
