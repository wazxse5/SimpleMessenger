package message.config;

import message.ServerMessage;

public class SessionAnswerMessage extends ServerMessage {
    private static final long serialVersionUID = 3465662164063416415L;
    private final int idSession;

    public SessionAnswerMessage(int idSession) {
        this.idSession = idSession;
    }

    public int getIdSession() {
        return idSession;
    }
}
