package chat.server.command;

import chat.server.client.Client;
import chat.server.user.Role;
import chat.server.user.UserService;

public class AccessValidator {
    private final UserService userService;

    public AccessValidator(UserService userService) {
        this.userService = userService;
    }

    public void validateAuthorized(Client client) {
        if (!client.isAuthorized()) {
            throw new NotAuthorizedAccessException();
        }
    }

    public void validateAdmin(Client client) {
        validateAuthorized(client);
        if (userService.getRole(client.getMe()) != Role.ADMIN) {
            throw new ForbiddenAccessException("you are not an admin!");
        }
    }

    public void validateModeratorOrAdmin(Client client) {
        validateAuthorized(client);
        Role role = userService.getRole(client.getMe());
        if (role != Role.MODERATOR && role != Role.ADMIN) {
            throw new ForbiddenAccessException("you are not a moderator or an admin!");
        }
    }
}
