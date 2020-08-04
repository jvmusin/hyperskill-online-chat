package chat.server.command;

import chat.server.chat.ChatStatistics;
import chat.server.client.Client;
import chat.server.message.MessageService;
import chat.server.message.MultilineMessage;
import chat.server.message.ServerMessage;

public class StatsCommand implements Command {
    private final AccessValidator accessValidator;
    private final MessageService messageService;

    public StatsCommand(AccessValidator accessValidator, MessageService messageService) {
        this.accessValidator = accessValidator;
        this.messageService = messageService;
    }

    @Override
    public void execute(Client client, String with) {
        accessValidator.validateAuthorized(client);

        ChatStatistics statistics = messageService.getStatistics(client.getActiveChat());
        if (!statistics.getYou().equals(client.getMe())) statistics = statistics.swapUsers();
        client.send(new MultilineMessage(new ServerMessage(statistics.toString())));
    }
}
