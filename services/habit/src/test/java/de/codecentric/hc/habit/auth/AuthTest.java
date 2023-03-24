package de.codecentric.hc.habit.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.codecentric.hc.habit.common.HttpHeaders;
import de.codecentric.hc.habit.habits.Habit;
import de.codecentric.hc.habit.habits.Habit.Schedule;
import de.codecentric.hc.habit.habits.Habit.Schedule.Frequency;
import de.codecentric.hc.habit.habits.HabitRepository;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthTest {

  private static final String DEFAULT_USER = "default";
  private static final Schedule DEFAULT_SCHEDULE =
      Schedule.builder().repetitions(1).frequency(Frequency.WEEKLY).build();
  private static final List<Habit> DEFAULT_HABITS =
      Arrays.asList(
          Habit.builder()
              .id(123l)
              .name("ABC")
              .userId(DEFAULT_USER)
              .schedule(DEFAULT_SCHEDULE)
              .build());
  private static final HttpHeaders DEFAULT_HEADERS = new HttpHeaders();

  static {
    DEFAULT_HEADERS.setBearerAuth(
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkZWZhdWx0In0.2E88ZlFE4Tor8d5gRU2451WrLtDavGfgbFf8ZuKBRxM");
  }

  @MockBean private HabitRepository repository;

  @Autowired private MockMvc mvc;

  @Test
  public void getHabitsWithUserIdHeaderShouldReturnOk() throws Exception {
    given(this.repository.findAllByUserIdOrderByNameAsc(eq(DEFAULT_USER)))
        .willReturn(DEFAULT_HABITS);
    mvc.perform(get("/habits").accept(APPLICATION_JSON).header(HttpHeaders.USER_ID, DEFAULT_USER))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*.name", containsInAnyOrder("ABC")));
    verify(repository).findAllByUserIdOrderByNameAsc(DEFAULT_USER);
  }

  @Test
  public void getHabitsWithUserIdHeaderAndJwtShouldUseJwtAndReturnOk() throws Exception {
    given(this.repository.findAllByUserIdOrderByNameAsc(eq(DEFAULT_USER)))
        .willReturn(DEFAULT_HABITS);
    mvc.perform(
            get("/habits")
                .accept(APPLICATION_JSON)
                .header(HttpHeaders.USER_ID, "other user")
                .headers(DEFAULT_HEADERS))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*.name", containsInAnyOrder("ABC")));
    verify(repository).findAllByUserIdOrderByNameAsc(DEFAULT_USER);
  }

  @Test
  public void getHabitsWithJwtShouldReturnOk() throws Exception {
    given(this.repository.findAllByUserIdOrderByNameAsc(eq(DEFAULT_USER)))
        .willReturn(DEFAULT_HABITS);
    mvc.perform(get("/habits").accept(APPLICATION_JSON).headers(DEFAULT_HEADERS))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*.name", containsInAnyOrder("ABC")));
  }

  @Test
  public void getHabitsWithoutAuthShouldThrowConstraintViolationException() throws Exception {
    Throwable thrown =
        catchThrowable(
            () ->
                mvc.perform(get("/habits").accept(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()));
    assertThat(thrown)
        .isInstanceOf(ServletException.class)
        .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
    assertThat(thrown.getCause())
        .hasMessage("getHabits.userId: must not be blank and size must be between 5 and 64");
  }
}
