package wazxse5.common.message.config;

public class GoodbyeMessage extends ServerMessage {
    private static final long serialVersionUID = -711089946246577100L;
    private final String goodbye;

    public GoodbyeMessage(String goodbye) {
        this.goodbye = goodbye;
    }

    public GoodbyeMessage() {
        this("goodbye");
    }

    public String getGoodbye() {
        return goodbye;
    }
}
