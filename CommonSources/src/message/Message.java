package message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private static final long serialVersionUID = -7677203165812975461L;
    private static int idCounter = 0;
    private final int id;

    public Message() {
        this.id = idCounter++;
    }

    public int getId() {
        return id;
    }
}
