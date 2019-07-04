package de.codecentric.hc.gateway.security;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum ApplicationUser {
  DEFAULT(Username.DEFAULT, Username.DEFAULT, Role.USER),
  MONITORING(Username.MONITORING, Username.DEFAULT, Role.MONITORING);

  private String username;
  private String password;
  private String role;

  public static List<String> lptUsers =
      IntStream.rangeClosed(1, 100)
          .mapToObj(i -> String.format("user%06d", i))
          .collect(Collectors.toList());

  ApplicationUser(String username, String password, String role) {
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

  public String getRole() {
    return role;
  }

  public interface Username {
    String DEFAULT = "default";
    String MONITORING = "monitoring";
  }

  public interface Role {
    String USER = "USER";
    String MONITORING = "MONITORING";
  }
}
