package chat.server.command;

import chat.server.client.Client;

import java.util.Map;

public class CommandRegister {
    private final Map<String, Command> commands;

    public CommandRegister(Map<String, Command> commands) {
        this.commands = commands;
    }

    public void executeCommand(String name, Client client, String params) {
        Command command = commands.get(name);
        if (command == null) throw new NoSuchCommandException();
        command.execute(client, params);
    }
}
