package exception;

public class NoSuchUserException extends AuthenticationException {

    public NoSuchUserException() {
        super(2);
    }
}
