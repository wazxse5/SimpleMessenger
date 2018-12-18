package wazxse5.common;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = -3865294124979558539L;
    private final String name;
    private final String surname;
    private final String mail;
    private final String login;
    private final boolean guest;

    public UserInfo(String name, String surname, String mail, String login, boolean guest) {
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.login = login;
        this.guest = guest;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getMail() {
        return mail;
    }

    public String getLogin() {
        return login;
    }

    public boolean isGuest() {
        return guest;
    }
}
