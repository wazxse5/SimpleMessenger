package message.config;

public class LoginAnswerMessage extends ServerMessage {
    private static final long serialVersionUID = 6846450951913670969L;
    private final boolean good;
    private final int infoCode;

    private final String name;
    private final String surname;
    private final String mail;
    private final String login;

    public LoginAnswerMessage(boolean good, int infoCode, String name, String surname, String mail, String login) {
        this.good = good;
        this.infoCode = infoCode;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.login = login;
    }

    public LoginAnswerMessage(boolean good, int infoCode) {
        this(good, infoCode, null, null, null, null);
    }

    public LoginAnswerMessage(boolean good) {
        this(good, 0, null, null, null, null);
    }

    public boolean isGood() {
        return good;
    }

    public int getInfoCode() {
        return infoCode;
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
