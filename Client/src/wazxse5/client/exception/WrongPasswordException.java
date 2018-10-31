package wazxse5.client.exception;

public class WrongPasswordException extends ConnectionException {

    public WrongPasswordException() {
        super("Nieprawidłowe hasło");
    }
}
