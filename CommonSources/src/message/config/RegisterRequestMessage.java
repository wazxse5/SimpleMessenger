package message.config;

public class RegisterRequestMessage extends ServerMessage {
    private static final long serialVersionUID = -7066000756945334003L;
    private final String name;
    private final String password;
    private final String mail;

    public RegisterRequestMessage(String name, String password, String mail) {
        this.name = name;
        this.password = password;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getMail() {
        return mail;
    }
}
