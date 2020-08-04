package chat.server.message;

public class MultilineMessage extends Message {
    public MultilineMessage(Message message) {
        super(message.getAuthor(), message.getContent());
    }

    @Override
    public String toString() {
        return String.format("%s:%n%s", getAuthor(), getContent());
    }
}
