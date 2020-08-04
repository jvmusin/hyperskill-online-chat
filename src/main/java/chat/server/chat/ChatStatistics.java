package chat.server.chat;

import java.util.StringJoiner;

public class ChatStatistics {
    private final String you;
    private final int messagesFromYou;
    private final String chatee;
    private final int messagesFromChatee;

    public ChatStatistics(String you, int messagesFromYou, String chatee, int messagesFromChatee) {
        this.you = you;
        this.messagesFromYou = messagesFromYou;
        this.chatee = chatee;
        this.messagesFromChatee = messagesFromChatee;
    }

    public String getYou() {
        return you;
    }

    public int getMessagesFromYou() {
        return messagesFromYou;
    }

    public String getChatee() {
        return chatee;
    }

    public int getMessagesFromChatee() {
        return messagesFromChatee;
    }

    public ChatStatistics swapUsers() {
        return new ChatStatistics(chatee, messagesFromChatee, you, messagesFromYou);
    }

    @Override
    public String toString() {
        StringJoiner res = new StringJoiner(System.lineSeparator());
        res.add(String.format("Statistics with %s:", chatee));
        res.add(String.format("Total messages: %d", messagesFromYou + messagesFromChatee));
        res.add(String.format("Messages from %s: %d", you, messagesFromYou));
        res.add(String.format("Messages from %s: %d", chatee, messagesFromChatee));
        return res.toString();
    }
}
