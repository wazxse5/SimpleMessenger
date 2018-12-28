package wazxse5.common.message.config;

import wazxse5.common.exception.AuthenticationException;

public class RegisterAnswerMessage extends ServerMessage {
    private static final long serialVersionUID = 577625073596981704L;
    private final boolean good;
    private final AuthenticationException exception;

    public RegisterAnswerMessage(boolean good, AuthenticationException exception) {
        this.good = good;
        this.exception = exception;
    }

    public boolean isGood() {
        return good;
    }

    public AuthenticationException getException() {
        return exception;
    }
}
