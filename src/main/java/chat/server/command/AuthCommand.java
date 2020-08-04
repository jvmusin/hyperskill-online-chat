package chat.server.command;

import chat.server.client.Client;
import chat.server.message.ServerMessage;
import chat.server.user.UserService;

public class AuthCommand implements Command {
    private final UserService userService;

    public AuthCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(Client client, String params) {
        String[] parts = params.split(" ");
        String login = parts[0];
        String password = parts[1];

        if (!userService.exists(login)) {
            client.send(new ServerMessage("incorrect login!"));
            return;
        }

        if (userService.isBanned(login)) {
            throw new ForbiddenAccessException("you are banned!");
        }

        int hash = userService.getPasswordHash(login);
        if (hash == password.hashCode()) {
            client.setMe(login);
            userService.addOnline(login);
            client.send(new ServerMessage("you are authorized successfully!"));
        } else {
            client.send(new ServerMessage("incorrect password!"));
        }
    }
}
