package message.config;

import message.ServerMessage;

public class WelcomeMessage extends ServerMessage {
    private static final long serialVersionUID = 7594607177957975646L;
    private final String greeting;

    public WelcomeMessage(String greeting) {
        this.greeting = greeting;
    }

    public String getGreeting() {
        return greeting;
    }
}
