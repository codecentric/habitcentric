package de.codecentric.hc.gateway.security;

public enum ApplicationUser {
    DEFAULT("default", "default", ApplicationUserRole.USER),
    MONITORING("monitoring", "monitoring", ApplicationUserRole.MONITORING);

    private String name;
    private String password;
    private ApplicationUserRole role;

    ApplicationUser(String name, String password, ApplicationUserRole role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public ApplicationUserRole getRole() {
        return role;
    }
}
