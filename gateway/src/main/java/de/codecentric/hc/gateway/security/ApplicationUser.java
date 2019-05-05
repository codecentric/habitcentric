package de.codecentric.hc.gateway.security;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum ApplicationUser {
    DEFAULT("default", "default", ApplicationUserRole.USER),
    MONITORING("monitoring", "monitoring", ApplicationUserRole.MONITORING);

    private String username;
    private String password;
    private ApplicationUserRole role;

    public static List<String> lptUsers = IntStream.rangeClosed(1, 100)
            .mapToObj(i -> String.format("user%06d", i))
            .collect(Collectors.toList());

    ApplicationUser(String username, String password, ApplicationUserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ApplicationUserRole getRole() {
        return role;
    }
}
