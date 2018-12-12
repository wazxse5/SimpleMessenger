package wazxse5.common.message.config;

public class LoginRequestMessage extends ServerMessage {
    private static final long serialVersionUID = -8511670995169064933L;
    private final String name;
    private final String password;
    private final boolean guest;

    public LoginRequestMessage(String name, String password, boolean guest) {
        this.name = name;
        this.password = password;
        this.guest = guest;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isGuest() {
        return guest;
    }
}
