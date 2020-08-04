package chat.server;

import chat.server.client.Client;
import chat.server.client.ClientService;
import chat.server.command.*;
import chat.server.message.MessageService;
import chat.server.user.Role;
import chat.server.user.UserService;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Server {

    private static final String DB_FILE_NAME = "server.db";
    private final ClientService clientService = new ClientService();
    private final CommandRegister commandRegister;
    private final UserService userService;
    private final MessageService messageService;

    public Server() {
        ServerState serverState = null;
        try (ObjectInputStream os = new ObjectInputStream(new FileInputStream(DB_FILE_NAME))) {
            serverState = (ServerState) os.readObject();
            System.err.println("Server state successfully read from db");
        } catch (Exception e) {
            System.err.println("Unable to read state from db: " + e.getMessage());
        }

        if (serverState != null) {
            userService = new UserService(serverState.getUsers());
            messageService = new MessageService(clientService, serverState.getHistory());
        } else {
            userService = new UserService();
            messageService = new MessageService(clientService);

            userService.saveUser("admin", "12345678");
            userService.setRole("admin", Role.ADMIN);
        }

        AccessValidator accessValidator = new AccessValidator(userService);
        commandRegister = new CommandRegister(Map.of(
                "/auth", new AuthCommand(userService),
                "/registration", new RegistrationCommand(userService),
                "/list", new ListCommand(accessValidator, userService),
                "/chat", new ChatCommand(accessValidator, userService, messageService),
                "/unread", new UnreadCommand(accessValidator, messageService),
                "/kick", new KickCommand(accessValidator, userService, clientService, messageService),
                "/grant", new GrantCommand(accessValidator, userService, messageService),
                "/revoke", new RevokeCommand(accessValidator, userService, messageService),
                "/stats", new StatsCommand(accessValidator, messageService),
                "/history", new HistoryCommand(accessValidator, messageService)
        ));
    }

    public static void main(String[] args) throws Exception {
        new Server().run();
    }

    private void saveServerState() throws IOException {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(DB_FILE_NAME))) {
            os.writeObject(new ServerState(userService.getUsers(), messageService.getHistory()));
        }
    }

    public void run() throws Exception {
        String address = "127.0.0.1";
        int port = 23456;
        try (ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address))) {

            System.out.println("Server started!");

            new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        Socket socket = serverSocket.accept();
                        Client client = new Client(socket, commandRegister, userService, messageService);
                        clientService.registerClient(client);
                        client.start();
                    }
                } catch (IOException ignored) {
                }
            }).start();

            TimeUnit.DAYS.sleep(1);
        } finally {
            saveServerState();
        }
    }
}
