package chat.server;

import chat.server.chat.Chat;
import chat.server.chat.ChatHistory;
import chat.server.user.UserDetails;

import java.io.Serializable;
import java.util.Map;

public class ServerState implements Serializable {
    private final Map<String, UserDetails> users;
    private final Map<Chat, ChatHistory> history;

    public ServerState(Map<String, UserDetails> users, Map<Chat, ChatHistory> history) {
        this.users = users;
        this.history = history;
    }

    public Map<String, UserDetails> getUsers() {
        return users;
    }

    public Map<Chat, ChatHistory> getHistory() {
        return history;
    }
}
