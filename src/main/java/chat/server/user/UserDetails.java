package chat.server.user;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserDetails implements Serializable {
    private String login;
    private int passwordHash;
    private Role role;
    private LocalDateTime bannedUntil;

    public UserDetails(String login, int passwordHash, Role role, LocalDateTime bannedUntil) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
        this.bannedUntil = bannedUntil;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(int passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(LocalDateTime bannedUntil) {
        this.bannedUntil = bannedUntil;
    }
}
