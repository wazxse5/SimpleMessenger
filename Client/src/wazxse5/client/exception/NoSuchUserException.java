package wazxse5.client.exception;

public class NoSuchUserException extends ConnectionException {

    public NoSuchUserException() {
        super("Nie ma takiego użytkownika");
    }
}
