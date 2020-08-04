package chat.server.command;

public class NotAuthorizedAccessException extends ForbiddenAccessException {
    public static final String MESSAGE = "you are not in the chat!";
    public NotAuthorizedAccessException() {
        super(MESSAGE);
    }
}
