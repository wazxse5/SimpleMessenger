package wazxse5.common.exception;

public class WrongPasswordException extends AuthenticationException {
    private static final long serialVersionUID = -2846350771562891145L;

    public WrongPasswordException() {
        super(3);
    }
}
