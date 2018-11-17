package wazxse5.server.message.config;

import wazxse5.server.message.ServerMessage;

public class RegisterRequestMessage extends ServerMessage {
    private String name;
    private String password;
    private String mail;

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
