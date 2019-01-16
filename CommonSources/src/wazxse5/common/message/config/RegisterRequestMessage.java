package wazxse5.common.message.config;

import wazxse5.common.UserInfo;

public class RegisterRequestMessage extends ServerMessage {
    private static final long serialVersionUID = -7066000756945334003L;
    private final UserInfo userInfo;
    private final String password;

    public RegisterRequestMessage(UserInfo userInfo, String password) {
        this.userInfo = userInfo;
        this.password = password;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getPassword() {
        return password;
    }
}
