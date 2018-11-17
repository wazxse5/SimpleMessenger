package wazxse5.server.message.config;

import wazxse5.server.message.ServerMessage;

public class LoginAnswerMessage extends ServerMessage {
    private boolean good;
    private byte infoCode;

    public LoginAnswerMessage(boolean good) {
        this.good = good;
    }

    public LoginAnswerMessage(boolean good, byte infoCode) {
        this.good = good;
        this.infoCode = infoCode;
    }

    public boolean isGood() {
        return good;
    }

    public int getInfoCode() {
        return (int) infoCode;
    }
}
