package wazxse5.common.exception;

public class WrongPasswordException extends AuthenticationException {
    private static final long serialVersionUID = -2846350771562891145L;
    private final int attemptsLeft;

    public WrongPasswordException(int attemptsLeft) {
        super(3);
        this.attemptsLeft = attemptsLeft;
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }
}
