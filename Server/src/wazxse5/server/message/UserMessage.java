package wazxse5.server.message;

public class UserMessage extends Message {
    private String from;
    private String to;
    private String message;

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
