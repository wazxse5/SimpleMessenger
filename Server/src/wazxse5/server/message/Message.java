package wazxse5.server.message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private static int idCounter = 0;
    private int id;

    public Message() {
        this.id = idCounter++;
    }

    public int getId() {
        return id;
    }
}
