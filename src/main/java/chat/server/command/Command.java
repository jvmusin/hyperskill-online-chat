package chat.server.command;

import chat.server.client.Client;

public interface Command {
    void execute(Client client, String params);
}
