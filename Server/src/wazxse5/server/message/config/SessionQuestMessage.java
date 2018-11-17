package wazxse5.server.message.config;

import wazxse5.server.message.ServerMessage;

public class SessionQuestMessage extends ServerMessage {
    private int idSession;
    private String[] conncectedClients;

    public SessionQuestMessage(int idSession, String[] conncectedClients) {
        this.idSession = idSession;
        this.conncectedClients = conncectedClients;
    }

    public int getIdSession() {
        return idSession;
    }

    public String[] getConncectedClients() {
        return conncectedClients;
    }
}
