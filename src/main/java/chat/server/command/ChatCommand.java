package chat.server.command;

import chat.server.chat.Chat;
import chat.server.client.Client;
import chat.server.message.MessageService;
import chat.server.message.ServerMessage;
import chat.server.user.UserService;

public class ChatCommand implements Command {
    private final AccessValidator accessValidator;
    private final UserService userService;
    private final MessageService messageService;

    public ChatCommand(AccessValidator accessValidator, UserService userService, MessageService messageService) {
        this.accessValidator = accessValidator;
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Client client, String with) {
        accessValidator.validateAuthorized(client);

        String me = client.getMe();
        if (me.equals(with)) {
            throw new IllegalArgumentException("You can't chat with yourself!");
        }

        if (userService.isOnline(with)) {
            Chat chat = new Chat(me, with);
            client.setActiveChat(chat);
            messageService.getInitialMessages(me, chat).forEach(client::send);
        } else {
            client.send(new ServerMessage("the user is not online!"));
        }
    }
}
