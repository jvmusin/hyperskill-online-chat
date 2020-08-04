package chat.server.command;

import chat.server.client.Client;
import chat.server.message.MessageService;
import chat.server.message.ServerMessage;
import chat.server.user.Role;
import chat.server.user.UserService;

public class RevokeCommand implements Command {
    private final AccessValidator accessValidator;
    private final UserService userService;
    private final MessageService messageService;

    public RevokeCommand(AccessValidator accessValidator, UserService userService, MessageService messageService) {
        this.accessValidator = accessValidator;
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Client client, String user) {
        accessValidator.validateAdmin(client);
        if (userService.getRole(user) != Role.MODERATOR) {
            throw new IllegalArgumentException("this user is not a moderator!");
        }
        userService.setRole(user, Role.USER);
        client.send(new ServerMessage(user + " is no longer a moderator!"));
        messageService.sendServerMessage(user, "you are no longer a moderator!");
    }
}
