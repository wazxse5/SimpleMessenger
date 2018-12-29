package wazxse5.common.message.config;

import java.util.List;

public class SessionMessage extends ServerMessage {
    private static final long serialVersionUID = 3320601889421372773L;
    private final int idSession;
    private final List<String> loggedUserNames;

    public SessionMessage(int idSession, List<String> loggedUserNames) {
        this.idSession = idSession;
        this.loggedUserNames = loggedUserNames;
    }

    public int getIdSession() {
        return idSession;
    }

    public List<String> getLoggedUserNames() {
        return loggedUserNames;
    }
}
