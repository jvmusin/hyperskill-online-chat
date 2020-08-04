package chat.server.command;

import chat.server.client.Client;
import chat.server.message.ServerMessage;
import chat.server.user.UserService;

public class RegistrationCommand implements Command {
    private final UserService userService;

    public RegistrationCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(Client client, String params) {
        String[] parts = params.split(" ");
        String login = parts[0];
        String password = parts[1];
        if (password.length() < 8) {
            client.send(new ServerMessage("the password is too short!"));
            return;
        }
        if (userService.exists(login)) {
            client.send(new ServerMessage("this login is already taken!"));
            return;
        }
        userService.saveUser(login, password);
        userService.addOnline(login);
        client.setMe(login);
        client.send(new ServerMessage("you are registered successfully!"));
    }
}
