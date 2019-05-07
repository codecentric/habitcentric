package de.codecentric.hc.track.habits;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class HabitTrackingControllerWebMvcTest {

    private final String urlTemplate = "/track/users/{userId}/habits/{habitId}";
    private final String userId = "abc.def";
    private final Long habitId = 123L;

    private final List<HabitTracking> defaultTrackRecords = Arrays.asList(
            new HabitTracking(userId, habitId, LocalDate.of(2019, JANUARY, 31)),
            new HabitTracking(userId, habitId, LocalDate.of(2018, DECEMBER, 31)),
            new HabitTracking(userId, habitId, LocalDate.of(2019, JANUARY, 1))
    );

    private final String expected = "[\"2018-12-31\",\"2019-01-01\",\"2019-01-31\"]";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HabitTrackingRepository repository;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    public void shouldReturnTrackRecords() throws Exception {
        given(repository.findByIdUserIdAndIdHabitId(userId, habitId)).willReturn(defaultTrackRecords);
        mockMvc.perform(get(urlTemplate, userId, habitId))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    public void shouldReturnEmptyArrayWhenTrackRecordsAreNotFound() throws Exception {
        given(repository.findByIdUserIdAndIdHabitId(userId, habitId)).willReturn(new ArrayList<>());
        mockMvc.perform(get(urlTemplate, userId, habitId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void shouldFilterOutDuplicateTrackRecords() throws Exception {
        given(repository.saveAll(anyIterable())).willReturn(defaultTrackRecords);
        mockMvc.perform(put(urlTemplate, userId, habitId)
                .contentType(APPLICATION_JSON)
                .content("[\"2019-01-31\",\"2019-01-01\",\"2018-12-31\",\"2019-01-31\",\"2018-12-31\"]"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}
