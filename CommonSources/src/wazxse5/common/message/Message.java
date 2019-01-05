package wazxse5.common.message;

import java.io.Serializable;
import java.util.Random;

public abstract class Message implements Serializable {
    private static final long serialVersionUID = -7677203165812975461L;
    private static final Random random = new Random();
    private final int id;

    public Message() {
        this.id = random.nextInt();
    }

    public int getId() {
        return id;
    }
}
