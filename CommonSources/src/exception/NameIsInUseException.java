package exception;

public class NameIsInUseException extends AuthenticationException {

    public NameIsInUseException() {
        super(1);
    }
}
