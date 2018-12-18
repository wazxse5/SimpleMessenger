package wazxse5.common.exception;

public class LoginNotExistsException extends AuthenticationException {
    private static final long serialVersionUID = -7440664147181854811L;

    public LoginNotExistsException() {
        super(2);
    }
}
