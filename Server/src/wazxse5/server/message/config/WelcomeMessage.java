package wazxse5.server.message.config;

import wazxse5.server.message.ServerMessage;

public class WelcomeMessage extends ServerMessage {
    private String greeting;

    public WelcomeMessage(String greeting) {
        this.greeting = greeting;
    }

    public String getGreeting() {
        return greeting;
    }
}
