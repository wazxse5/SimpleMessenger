package wazxse5.server.message.config;

import wazxse5.server.message.ServerMessage;

public class SessionAnswerMessage extends ServerMessage {
    private int idSession;

    public SessionAnswerMessage(int idSession) {
        this.idSession = idSession;
    }

    public int getIdSession() {
        return idSession;
    }
}
