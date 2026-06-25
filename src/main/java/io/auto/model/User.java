package io.auto.model;

import io.auto.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String username;
    private String password;
    private Role role;
    private int highValueThreshold;

    public User(String name, String username, String password, Role role, int highValueThreshold) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.highValueThreshold = highValueThreshold;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getHighValueThreshold() {
        return highValueThreshold;
    }

    public void setHighValueThreshold(int highValueThreshold) {
        this.highValueThreshold = highValueThreshold;
    }
}
