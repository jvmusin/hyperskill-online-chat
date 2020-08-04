package chat.server.message;

import chat.server.chat.Chat;
import chat.server.chat.ChatHistory;
import chat.server.chat.ChatStatistics;
import chat.server.client.ClientService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

public class MessageService implements Serializable {
    private final ClientService clientService;
    private final Map<Chat, ChatHistory> history;

    public MessageService(ClientService clientService, Map<Chat, ChatHistory> history) {
        this.clientService = clientService;
        this.history = history;
    }

    public MessageService(ClientService clientService) {
        this(clientService, new ConcurrentHashMap<>());
    }

    private ChatHistory getHistory(Chat chat) {
        return history.computeIfAbsent(chat, k -> new ChatHistory(k));
    }

    public void sendMessage(Chat chat, Message message) {
        ChatHistory history = getHistory(chat);
        history.add(message);
        clientService.getClientsForChat(chat).forEach(c -> {
            if (c.getMe().equals(message.getAuthor())) c.send(message);
            else c.send(new NewMessage(message));
            history.markAllRead(c.getMe());
        });
    }

    public void sendServerMessage(String to, String message) {
        clientService.getClientByUser(to).ifPresent(u -> u.send(new ServerMessage(message)));
    }

    public List<String> getUnreadChats(String user) {
        return history.entrySet().stream()
                .filter(e -> e.getKey().contains(user) && e.getValue().anyUnreadMessages(user))
                .map(e -> e.getKey().getAnotherUser(user))
                .collect(toList());
    }

    public List<Message> getLastMessages(Chat chat, int count) {
        return getHistory(chat).getLastMessages(count);
    }

    public List<Message> getInitialMessages(String user, Chat chat) {
        return getHistory(chat).getInitialMessages(user);
    }

    public ChatStatistics getStatistics(Chat chat) {
        return getHistory(chat).getStatistics();
    }

    public Map<Chat, ChatHistory> getHistory() {
        return history;
    }
}
