package wazxse5.common.exception;

public class NoSuchUserException extends AuthenticationException {
    private static final long serialVersionUID = -7440664147181854811L;

    public NoSuchUserException() {
        super(2);
    }
}
