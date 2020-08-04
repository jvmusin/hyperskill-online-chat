package chat.server.command;

import chat.server.chat.Chat;
import chat.server.client.Client;
import chat.server.message.Message;
import chat.server.message.MessageService;
import chat.server.message.MultilineMessage;
import chat.server.message.ServerMessage;

import java.util.List;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class HistoryCommand implements Command {
    private final AccessValidator accessValidator;
    private final MessageService messageService;

    public HistoryCommand(AccessValidator accessValidator, MessageService messageService) {
        this.accessValidator = accessValidator;
        this.messageService = messageService;
    }

    @Override
    public void execute(Client client, String countStr) {
        accessValidator.validateAuthorized(client);

        int count;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(countStr + " is not a number!");
        }
        Chat chat = client.getActiveChat();
        List<Message> lastMessages = messageService.getLastMessages(chat, count);
        String messagesStr = lastMessages.stream().map(Object::toString).collect(joining(lineSeparator()));
        client.send(new MultilineMessage(new ServerMessage(messagesStr)));
    }
}
