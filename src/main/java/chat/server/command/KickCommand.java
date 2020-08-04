package chat.server.command;

import chat.server.client.Client;
import chat.server.client.ClientService;
import chat.server.message.MessageService;
import chat.server.message.ServerMessage;
import chat.server.user.UserService;

import java.time.LocalDateTime;

public class KickCommand implements Command {
    private final AccessValidator accessValidator;
    private final UserService userService;
    private final ClientService clientService;
    private final MessageService messageService;

    public KickCommand(AccessValidator accessValidator, UserService userService, ClientService clientService, MessageService messageService) {
        this.accessValidator = accessValidator;
        this.userService = userService;
        this.clientService = clientService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Client client, String user) {
        accessValidator.validateModeratorOrAdmin(client);
        if (client.getMe().equals(user)) {
            throw new IllegalArgumentException("you can't kick yourself!");
        }
        userService.setBannedUntil(user, LocalDateTime.now().plusMinutes(5));
        userService.removeOnline(user);
        client.send(new ServerMessage(user + " was kicked!"));
        messageService.sendServerMessage(user, "you have been kicked out of the server!");
        clientService.logOut(user);
    }
}
