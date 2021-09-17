package de.codecentric.hc.gateway.routing;

import static de.codecentric.hc.gateway.security.ApplicationUser.Role.USER;
import static de.codecentric.hc.gateway.security.ApplicationUser.Username.DEFAULT;
import static org.hamcrest.Matchers.hasItems;

import de.codecentric.hc.gateway.testing.WebTest;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class TrackIntTest extends WebTest {

  @Test
  @WithMockUser(username = DEFAULT, roles = USER)
  public void getTrackRecordsShouldReturnTrackRecordsForHabits() {
    get("/track/habits/123")
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$[*]")
        .value(hasItems("2019-03-19", "2019-03-20", "2019-03-21"));
  }

  @Test
  @WithMockUser(username = DEFAULT, roles = USER)
  public void putTrackRecordsShouldUpsertTrackRecordsForHabits() {
    List<String> dates = Arrays.asList("2019-03-19", "2019-03-20", "2019-03-21");
    put("/track/habits/123", dates).expectStatus().isOk();
  }
}
