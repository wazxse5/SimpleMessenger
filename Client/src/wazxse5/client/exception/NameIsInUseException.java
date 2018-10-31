package wazxse5.client.exception;

public class NameIsInUseException extends ConnectionException {

    public NameIsInUseException() {
        super("Login jest już zajęty");
    }
}
