package chat.server.command;

import chat.server.client.Client;
import chat.server.message.MessageService;
import chat.server.message.ServerMessage;
import chat.server.user.Role;
import chat.server.user.UserService;

public class GrantCommand implements Command {
    private final AccessValidator accessValidator;
    private final UserService userService;
    private final MessageService messageService;

    public GrantCommand(AccessValidator accessValidator, UserService userService, MessageService messageService) {
        this.accessValidator = accessValidator;
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Client client, String user) {
        accessValidator.validateAdmin(client);
        if (userService.getRole(user) == Role.MODERATOR) {
            throw new IllegalArgumentException("this user is already a moderator!");
        }
        userService.setRole(user, Role.MODERATOR);
        client.send(new ServerMessage(user + " is the new moderator!"));
        messageService.sendServerMessage(user, "you are the new moderator now!");
    }
}
