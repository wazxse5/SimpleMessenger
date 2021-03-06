package wazxse5.common.message;

public class UserMessage extends Message {
    private static final long serialVersionUID = 6629966587887294896L;
    private final String from;
    private final String to;
    private final String message;

    public UserMessage(String from, String to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }
}
