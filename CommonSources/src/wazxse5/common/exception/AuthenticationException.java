package wazxse5.common.exception;

public abstract class AuthenticationException extends Exception {
    private static final long serialVersionUID = -228485412188331421L;
    private final int code;

    public AuthenticationException(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
