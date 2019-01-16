package wazxse5.common.message.config;

public class LoginRequestMessage extends ServerMessage {
    private static final long serialVersionUID = -8511670995169064933L;
    private final String login;
    private final String password;
    private final boolean guest;

    public LoginRequestMessage(String login, String password, boolean guest) {
        this.login = login;
        this.password = password;
        this.guest = guest;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isGuest() {
        return guest;
    }
}
