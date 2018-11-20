package message.config;

import message.ServerMessage;

import java.util.List;

public class SessionMessage extends ServerMessage {
    private static final long serialVersionUID = 3320601889421372773L;
    private final int idSession;
    private final List<String> conncectedClientsNames;

    public SessionMessage(int idSession, List<String> conncectedClientsNames) {
        this.idSession = idSession;
        this.conncectedClientsNames = conncectedClientsNames;
    }

    public int getIdSession() {
        return idSession;
    }

    public List<String> getConncectedClientsNames() {
        return conncectedClientsNames;
    }
}
