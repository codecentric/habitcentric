package de.codecentric.hc.habit.jwt;

import de.codecentric.hc.habit.validation.UserId;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class User {

    @UserId
    String userId;
}
