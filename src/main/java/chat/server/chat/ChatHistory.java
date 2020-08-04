package chat.server.chat;

import chat.server.message.Message;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatHistory implements Serializable {
    private final Chat chat;
    private final List<Message> messages = new CopyOnWriteArrayList<>();
    private final int[] readCount = {0, 0};

    public ChatHistory(Chat chat) {
        this.chat = chat;
    }

    public void add(Message message) {
        messages.add(message);
    }

    public int getUnreadMessagesCount(String user) {
        return messages.size() - readCount[chat.getUserIndex(user)];
    }

    public boolean anyUnreadMessages(String user) {
        return readCount[chat.getUserIndex(user)] < messages.size();
    }

    public List<Message> getInitialMessages(String user) {
        int index = chat.getUserIndex(user);
        int unreadMessagesCount = getUnreadMessagesCount(user);
        int toReturnCount = Math.min(Math.min(25, messages.size()), unreadMessagesCount + 10);
        readCount[index] = messages.size();
        return getLastMessages(toReturnCount);
    }

    public List<Message> getLastMessages(int count) {
        int from = Math.max(0, messages.size() - count);
        int to = Math.min(messages.size(), from + Math.min(25, count));
        return messages.subList(from, to);
    }

    public ChatStatistics getStatistics() {
        int countFromFirst = (int) messages.stream()
                .filter(m -> m.getAuthor().equals(chat.getFirst()))
                .count();
        return new ChatStatistics(
                chat.getFirst(),
                countFromFirst,
                chat.getSecond(),
                messages.size() - countFromFirst
        );
    }

    public void markAllRead(String user) {
        readCount[chat.getUserIndex(user)] = messages.size();
    }
}
