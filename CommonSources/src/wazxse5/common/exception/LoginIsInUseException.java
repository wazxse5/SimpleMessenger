package wazxse5.common.exception;

public class LoginIsInUseException extends AuthenticationException {
    private static final long serialVersionUID = 8861638979891053412L;

    public LoginIsInUseException() {
        super(1);
    }
}
