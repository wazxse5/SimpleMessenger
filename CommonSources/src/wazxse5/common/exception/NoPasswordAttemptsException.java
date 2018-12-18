package wazxse5.common.exception;

public class NoPasswordAttemptsException extends AuthenticationException {
    private static final long serialVersionUID = -3927681236081581356L;

    public NoPasswordAttemptsException() {
        super(4);
    }
}
