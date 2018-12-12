package wazxse5.common.message.config;

import wazxse5.common.UserInfo;

public class LoginAnswerMessage extends ServerMessage {
    private static final long serialVersionUID = 6846450951913670969L;
    private final boolean good;
    private final int infoCode;
    private final UserInfo userInfo;

    public LoginAnswerMessage(boolean good, int infoCode, UserInfo userInfo) {
        this.good = good;
        this.infoCode = infoCode;
        this.userInfo = userInfo;
    }

    public LoginAnswerMessage(boolean good, int infoCode) {
        this(good, infoCode, null);
    }

    public LoginAnswerMessage(boolean good) {
        this(good, 0, null);
    }

    public boolean isGood() {
        return good;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
