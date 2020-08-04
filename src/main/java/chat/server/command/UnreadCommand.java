package chat.server.command;

import chat.server.client.Client;
import chat.server.message.MessageService;
import chat.server.message.ServerMessage;

import java.util.List;
import java.util.stream.Collectors;

public class UnreadCommand implements Command {
    private final AccessValidator accessValidator;
    private final MessageService messageService;

    public UnreadCommand(AccessValidator accessValidator, MessageService messageService) {
        this.accessValidator = accessValidator;
        this.messageService = messageService;
    }

    @Override
    public void execute(Client client, String params) {
        accessValidator.validateAuthorized(client);

        List<String> unreadChats = messageService.getUnreadChats(client.getMe());
        if (unreadChats.isEmpty()) {
            client.send(new ServerMessage("no one unread"));
        } else {
            String unreadStr = unreadChats.stream().sorted().collect(Collectors.joining(" "));
            client.send(new ServerMessage("unread from: " + unreadStr));
        }
    }
}
