package chat.server.client;

import chat.server.chat.Chat;
import chat.server.command.CommandRegister;
import chat.server.command.ForbiddenAccessException;
import chat.server.command.NoSuchCommandException;
import chat.server.command.NotAuthorizedAccessException;
import chat.server.message.Message;
import chat.server.message.MessageService;
import chat.server.message.ServerMessage;
import chat.server.user.UserService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {
    private final Socket socket;
    private final CommandRegister commandRegister;
    private final UserService userService;
    private final MessageService messageService;
    private String me;
    private Chat activeChat;
    private volatile DataOutputStream os;

    public Client(Socket socket, CommandRegister commandRegister, UserService userService, MessageService messageService) {
        this.socket = socket;
        this.commandRegister = commandRegister;
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void run() {
        try (DataInputStream is = new DataInputStream(socket.getInputStream());
             DataOutputStream ignored = this.os = new DataOutputStream(socket.getOutputStream())) {
            send(new ServerMessage("authorize or register"));

            while (!isInterrupted()) {
                String message = is.readUTF();
                if (message.equals("/exit")) break;
                if (isCommand(message)) {
                    String[] parts = message.split(" ", 2);
                    try {
                        commandRegister.executeCommand(parts[0], this, parts.length == 1 ? null : parts[1]);
                    } catch (NoSuchCommandException e) {
                        send(new ServerMessage("incorrect command!"));
                    } catch (NotAuthorizedAccessException e) {
                        sendNotAuthorizedMessage();
                    } catch (ForbiddenAccessException | IllegalArgumentException e) {
                        send(new ServerMessage(e.getMessage()));
                    }
                } else {
                    if (!isAuthorized()) {
                        sendNotAuthorizedMessage();
                    } else if (activeChat == null) {
                        send(new ServerMessage("use /list command to choose an user to text!"));
                    } else {
                        messageService.sendMessage(activeChat, new Message(me, message));
                    }
                }
            }
        } catch (IOException e) {
            if (isAuthorized()) userService.removeOnline(me);
        }
    }

    public boolean isAuthorized() {
        return me != null;
    }

    private void sendNotAuthorizedMessage() {
        send(new ServerMessage(NotAuthorizedAccessException.MESSAGE));
    }

    private boolean isCommand(String s) {
        return s.startsWith("/");
    }

    public synchronized void send(Message message) {
        try {
            os.writeUTF(message.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMe() {
        return me;
    }

    public void setMe(String user) {
        me = user;
    }

    public Chat getActiveChat() {
        return activeChat;
    }

    public void setActiveChat(Chat activeChat) {
        this.activeChat = activeChat;
    }
}
