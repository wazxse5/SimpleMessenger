package wazxse5.common.message.config;

import wazxse5.common.UserInfo;
import wazxse5.common.exception.AuthenticationException;

import java.util.List;

public class LoginAnswerMessage extends ServerMessage {
    private static final long serialVersionUID = 6846450951913670969L;
    private final boolean good;
    private final AuthenticationException exception;
    private final UserInfo userInfo;
    private final List<String> connectedUsers;

    public LoginAnswerMessage(boolean good, AuthenticationException exception, UserInfo userInfo, List<String> connectedUsers) {
        this.good = good;
        this.exception = exception;
        this.userInfo = userInfo;
        this.connectedUsers = connectedUsers;
    }

    public LoginAnswerMessage(boolean good, AuthenticationException exception) {
        this(good, exception, null, null);
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
