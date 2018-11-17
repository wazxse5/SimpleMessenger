package wazxse5.server.message.config;

import wazxse5.server.message.ServerMessage;

public class RegisterAnswerMessage extends ServerMessage {
    private boolean ok;
    private byte infoCode;

    public RegisterAnswerMessage(byte infoCode) {
        this.infoCode = infoCode;
    }

    public RegisterAnswerMessage(boolean ok, byte infoCode) {
        this.ok = ok;
        this.infoCode = infoCode;
    }

    public boolean isOk() {
        return ok;
    }

    public int getInfoCode() {
        return (int) infoCode;
    }
}
