package wazxse5.common.message.config;

public class ConfirmationMessage extends ServerMessage {
    private static final long serialVersionUID = -1949233113975585444L;
    private int confirmedId;

    public ConfirmationMessage(int confirmedId) {
        this.confirmedId = confirmedId;
    }

    public int getConfirmedId() {
        return confirmedId;
    }
}
