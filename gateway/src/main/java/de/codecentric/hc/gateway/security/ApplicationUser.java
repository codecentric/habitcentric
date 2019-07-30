package de.codecentric.hc.gateway.security;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ApplicationUser {
  DEFAULT(Username.DEFAULT, Username.DEFAULT, Role.USER),
  MONITORING(Username.MONITORING, Username.DEFAULT, Role.MONITORING);

  @Getter private String username;
  @Getter private String password;
  @Getter private String role;

  public static List<String> lptUsers =
      IntStream.rangeClosed(1, 100)
          .mapToObj(i -> String.format("user%06d", i))
          .collect(Collectors.toList());

  public interface Username {
    String DEFAULT = "default";
    String MONITORING = "monitoring";
  }

  public interface Role {
    String USER = "USER";
    String MONITORING = "MONITORING";
  }
}
