package chat.server.chat;

import java.io.Serializable;
import java.util.Objects;

public class Chat implements Serializable {
    private final String first;
    private final String second;

    public Chat(String first, String second) {
        if (first.compareTo(second) < 0) {
            this.first = first;
            this.second = second;
        } else {
            this.first = second;
            this.second = first;
        }
    }

    public boolean contains(String user) {
        return first.equals(user) || second.equals(user);
    }

    public int getUserIndex(String user) {
        if (first.equals(user)) return 0;
        if (second.equals(user)) return 1;
        throw new IllegalArgumentException("User is not in this chat");
    }

    public String getAnotherUser(String user) {
        int index = getUserIndex(user);
        return index == 0 ? second : first;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(first, chat.first) &&
                Objects.equals(second, chat.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
