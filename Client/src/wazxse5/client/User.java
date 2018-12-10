package wazxse5.client;

public class User {
    private String name;
    private String surname;
    private String mail;
    private String login;

    public User(String name, String surname, String mail, String login) {
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.login = login;
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
}
