package wazxse5.common;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = -3865294124979558539L;
    private final int id;
    private final String login;
    private String mail;
    private String name;
    private String surname;
    private final LocalDateTime registrationTime;
    private int passwordCounter;
    private final boolean guest;

    public UserInfo(int id, String name, String surname, String mail, String login, LocalDateTime registrationTime, int passwordCounter, boolean guest) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.login = login;
        this.registrationTime = registrationTime;
        this.passwordCounter = passwordCounter;
        this.guest = guest;
    }

    public int getId() {
        return id;
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
