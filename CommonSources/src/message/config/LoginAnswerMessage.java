package message.config;

import message.ServerMessage;

public class LoginAnswerMessage extends ServerMessage {
    private static final long serialVersionUID = 6846450951913670969L;
    private final boolean good;
    private final int infoCode;

    public LoginAnswerMessage(boolean good) {
        this.good = good;
        this.infoCode = 0;
    }

    public LoginAnswerMessage(boolean good, int infoCode) {
        this.good = good;
        this.infoCode = infoCode;
    }

    public boolean isGood() {
        return good;
    }

    public int getInfoCode() {
        return infoCode;
    }
}
