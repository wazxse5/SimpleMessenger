package exception;

public abstract class AuthenticationException extends Exception {
    private final int code;

    public AuthenticationException(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
