package wazxse5.server.message.config;

import wazxse5.server.message.ServerMessage;

public class LoginRequestMessage extends ServerMessage {
    private String name;
    private String password;
    private boolean guest;

    public LoginRequestMessage(String name, String password) {
        this.name = name;
        this.password = password;
        this.guest = false;
    }

    public LoginRequestMessage(String name) {
        this.name = name;
        this.password = null;
        this.guest = true;
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
