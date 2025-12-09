package de.codecentric.habitcentric.track.habit;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class HabitTrackingControllerWebMvcTest {

  private final String urlTemplate = "/track/users/{userId}/habits/{habitId}";
  private final String userId = "abc.def";
  private final UUID habitId = UUID.randomUUID();

  private final HabitTracking defaultTrackRecords =
      HabitTracking.from(
          userId,
          habitId,
          Set.of(
              LocalDate.of(2019, JANUARY, 31),
              LocalDate.of(2018, DECEMBER, 31),
              LocalDate.of(2019, JANUARY, 1)));

  private final String expected = "[\"2018-12-31\",\"2019-01-01\",\"2019-01-31\"]";

  @Autowired private MockMvc mockMvc;

  @MockitoBean private HabitTrackingRepository repository;

  @MockitoBean private JwtDecoder jwtDecoder;

  @Test
  public void shouldReturnTrackRecords() throws Exception {
    given(repository.findByIdUserIdAndIdHabitId(userId, habitId))
        .willReturn(Optional.of(defaultTrackRecords));
    mockMvc
        .perform(get(urlTemplate, userId, habitId))
        .andExpect(status().isOk())
        .andExpect(content().json(expected));
  }

  @Test
  public void shouldReturnEmptyArrayWhenTrackRecordsAreNotFound() throws Exception {
    given(repository.findByIdUserIdAndIdHabitId(userId, habitId))
        .willReturn(Optional.of(HabitTracking.from(userId, habitId)));
    mockMvc
        .perform(get(urlTemplate, userId, habitId))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  @Test
  public void shouldFilterOutDuplicateTrackRecords() throws Exception {
    given(repository.findByIdUserIdAndIdHabitId(userId, habitId))
        .willReturn(Optional.of(defaultTrackRecords));
    given(repository.save(any())).willReturn(defaultTrackRecords);
    mockMvc
        .perform(
            put(urlTemplate, userId, habitId)
                .contentType(APPLICATION_JSON)
                .content(
                    "[\"2019-01-31\",\"2019-01-01\",\"2018-12-31\",\"2019-01-31\",\"2018-12-31\"]"))
        .andExpect(status().isOk())
        .andExpect(content().json(expected));
  }
}
