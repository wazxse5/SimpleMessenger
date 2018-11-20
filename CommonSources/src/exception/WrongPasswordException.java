package exception;

public class WrongPasswordException extends AuthenticationException {

    public WrongPasswordException() {
        super(3);
    }
}
