package chat.server.user;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class UserService implements Serializable {
    private final Map<String, UserDetails> users;
    private final Set<String> online = new ConcurrentSkipListSet<>();

    public UserService(Map<String, UserDetails> users) {
        this.users = users;
    }

    public UserService() {
        this(new ConcurrentHashMap<>());
    }

    public Set<String> getOnline() {
        return online;
    }

    public void addOnline(String user) {
        online.add(user);
    }

    public void removeOnline(String user) {
        online.remove(user);
    }

    public int getPasswordHash(String login) {
        return getUserExceptionally(login).getPasswordHash();
    }

    public Role getRole(String login) {
        return getUserExceptionally(login).getRole();
    }

    public void setRole(String login, Role role) {
        getUserExceptionally(login).setRole(role);
    }

    public boolean exists(String login) {
        return users.containsKey(login);
    }

    public void saveUser(String login, String password) {
        users.put(login, new UserDetails(login, password.hashCode(), Role.USER, null));
    }

    public void setBannedUntil(String login, LocalDateTime bannedUntil) {
        getUserExceptionally(login).setBannedUntil(bannedUntil);
    }

    public boolean isOnline(String user) {
        return online.contains(user);
    }

    public Map<String, UserDetails> getUsers() {
        return users;
    }

    public boolean isBanned(String login) {
        LocalDateTime bannedUntil = getUserExceptionally(login).getBannedUntil();
        return bannedUntil != null && LocalDateTime.now().isBefore(bannedUntil);
    }

    private UserDetails getUserExceptionally(String login) {
        UserDetails user = users.get(login);
        if (user == null) throw new IllegalArgumentException("User doesn't exist");
        return user;
    }
}
