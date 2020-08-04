package chat.server.message;

public class NewMessage extends Message {
    public NewMessage(Message message) {
        super(message.getAuthor(), message.getContent());
    }

    @Override
    public String toString() {
        return "(new) " + super.toString();
    }
}
