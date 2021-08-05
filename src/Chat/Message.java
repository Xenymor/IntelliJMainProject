package Chat;

import java.io.Serializable;

public class Message implements Serializable {
    private final String message;
    private final String userName;

    public Message(String message, String userName) {
        this.message = message;
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return userName;
    }
}
