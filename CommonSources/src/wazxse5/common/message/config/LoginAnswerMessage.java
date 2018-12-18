package wazxse5.common.message.config;

import wazxse5.common.UserInfo;
import wazxse5.common.exception.AuthenticationException;

public class LoginAnswerMessage extends ServerMessage {
    private static final long serialVersionUID = 6846450951913670969L;
    private final boolean good;
    private final AuthenticationException exception;
    private final UserInfo userInfo;

    public LoginAnswerMessage(boolean good, AuthenticationException exception, UserInfo userInfo) {
        this.good = good;
        this.exception = exception;
        this.userInfo = userInfo;
    }

    public boolean isGood() {
        return good;
    }

    public AuthenticationException getException() {
        return exception;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
