package chat.server.message;

import java.io.Serializable;

public class ServerMessage extends Message implements Serializable {
    public ServerMessage(String content) {
        super("Server", content);
    }
}
