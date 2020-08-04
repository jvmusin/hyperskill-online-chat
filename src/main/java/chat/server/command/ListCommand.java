package chat.server.command;

import chat.server.client.Client;
import chat.server.message.ServerMessage;
import chat.server.user.UserService;

import java.util.stream.Collectors;

public class ListCommand implements Command {
    private final AccessValidator accessValidator;
    private final UserService userService;

    public ListCommand(AccessValidator accessValidator, UserService userService) {
        this.accessValidator = accessValidator;
        this.userService = userService;
    }

    @Override
    public void execute(Client client, String params) {
        accessValidator.validateAuthorized(client);
        String online = userService.getOnline().stream()
                .filter(u -> !u.equals(client.getMe()))
                .collect(Collectors.joining(" "));
        if (online.isEmpty()) online = "no one online";
        else online = "online: " + online;
        client.send(new ServerMessage(online));
    }
}
