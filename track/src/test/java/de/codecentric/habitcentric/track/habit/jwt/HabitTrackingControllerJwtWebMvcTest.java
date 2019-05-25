package de.codecentric.habitcentric.track.habit.jwt;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.codecentric.habitcentric.track.habit.HabitTracking;
import de.codecentric.habitcentric.track.habit.HabitTrackingRepository;
import java.time.LocalDate;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest
public class HabitTrackingControllerJwtWebMvcTest {

  private final String urlTemplate = "/track/habits/{habitId}";
  private final String userId = "abc.def";
  private final String authorizationHeader = "Bearer _";
  private final Long habitId = 123L;

  private final List<HabitTracking> defaultTrackRecords =
      Arrays.asList(
          new HabitTracking(userId, habitId, LocalDate.of(2019, JANUARY, 31)),
          new HabitTracking(userId, habitId, LocalDate.of(2018, DECEMBER, 31)),
          new HabitTracking(userId, habitId, LocalDate.of(2019, JANUARY, 1)));

  private final String expected = "[\"2018-12-31\",\"2019-01-01\",\"2019-01-31\"]";

  @Autowired private MockMvc mockMvc;

  @MockBean private HabitTrackingRepository repository;

  @MockBean private JwtDecoder jwtDecoder;

  @Before
  public void beforeEach() {
    mockJwtDecoderWithValidUserId();
  }

  private void mockJwtDecoderWithValidUserId() {
    given(jwtDecoder.decode(any(String.class)))
        .willReturn(new Jwt("_", null, null, defaultHeaders(), claimsWithSubject()));
  }

  private Map<String, Object> defaultHeaders() {
    Map<String, Object> headers = new HashMap<>();
    headers.put("alg", "HS256");
    return headers;
  }

  private Map<String, Object> claimsWithSubject() {
    Map<String, Object> claims = new HashMap<>();
    claims.put("sub", userId);
    return claims;
  }

  @Test
  public void shouldReturnTrackRecords() throws Exception {
    given(repository.findByIdUserIdAndIdHabitId(userId, habitId)).willReturn(defaultTrackRecords);
    mockMvc
        .perform(get(urlTemplate, habitId).header(HttpHeaders.AUTHORIZATION, authorizationHeader))
        .andExpect(status().isOk())
        .andExpect(content().json(expected));
  }

  @Test
  public void shouldReturnEmptyArrayWhenTrackRecordsAreNotFound() throws Exception {
    given(repository.findByIdUserIdAndIdHabitId(userId, habitId)).willReturn(new ArrayList<>());
    mockMvc
        .perform(get(urlTemplate, habitId).header(HttpHeaders.AUTHORIZATION, authorizationHeader))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  @Test
  public void shouldFilterOutDuplicateTrackRecords() throws Exception {
    given(repository.saveAll(anyIterable())).willReturn(defaultTrackRecords);
    mockMvc
        .perform(
            put(urlTemplate, habitId)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .contentType(APPLICATION_JSON)
                .content(
                    "[\"2019-01-31\",\"2019-01-01\",\"2018-12-31\",\"2019-01-31\",\"2018-12-31\"]"))
        .andExpect(status().isOk())
        .andExpect(content().json(expected));
  }
}
