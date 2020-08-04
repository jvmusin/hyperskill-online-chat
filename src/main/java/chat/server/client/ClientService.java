package chat.server.client;

import chat.server.chat.Chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientService {
    private final List<Client> clients = Collections.synchronizedList(new ArrayList<>());

    private Stream<Client> clientsAlive() {
        return clients.stream().filter(Thread::isAlive);
    }

    private Stream<Client> authorizedClients() {
        return clientsAlive().filter(Client::isAuthorized);
    }

    public List<Client> getClientsForChat(Chat chat) {
        return authorizedClients().filter(c -> chat.equals(c.getActiveChat())).collect(Collectors.toList());
    }

    public Optional<Client> getClientByUser(String user) {
        return authorizedClients().filter(c -> c.getMe().equals(user)).findAny();
    }

    public void registerClient(Client client) {
        clients.add(client);
    }

    public void logOut(String user) {
        getClientByUser(user).ifPresent(c -> c.setMe(null));
    }
}
