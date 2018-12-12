package wazxse5.server;

import exception.AuthenticationException;
import exception.NameIsInUseException;
import exception.NoSuchUserException;
import exception.WrongPasswordException;

import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private List<User> users;

    public DataLoader() {
        users = new ArrayList<>();
        load();
    }

    public void load() {
        users.add(new User("test", false));
        users.add(new User("wazxse5", false));

        for (User user : users) for (User friend : users) if (user != friend) user.addFriend(friend);
    }

    public synchronized User register(String name, String password) throws AuthenticationException {
        for (User c : users) {
            if (c.getName().equals(name)) {
                throw new NameIsInUseException();
            }
        }
        User user = new User(name, false);
        users.add(user);
        return user;
    }

    public synchronized User login(String name, String password, boolean isGuest) throws AuthenticationException {
        User user;
        if (isGuest) user = loginAsGuest(name);
        else user = loginAsUser(name, password);
        return user;
    }

    private synchronized User loginAsGuest(String name) throws NameIsInUseException {
        for (User c : users) {
            if (c.getName().equals(name)) {
                throw new NameIsInUseException();
            }
        }
        User user = new User(name, true, users);
        users.add(user);
        return user;
    }

    private User loginAsUser(String name, String password) throws WrongPasswordException, NoSuchUserException {
        for (User c : users) {
            if (c.getName().equals(name)) {
                // TODO: Dodać sprawdzanie czy klient nie został już zaologowany
                if (checkPassword(c, password)) {
                    return c;
                } else throw new WrongPasswordException();
            }
        }
        throw new NoSuchUserException();
    }

    private boolean checkPassword(User user, String password) {
        // TODO: sprawdzanie hasła z pliku
        if (user.getName().equals("wazxse5") && password.equals("1234")) return true;
        else if (user.getName().equals("test") && password.equals("0000")) return true;
        else return false;
    }


}
