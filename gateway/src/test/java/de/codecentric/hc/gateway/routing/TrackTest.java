package de.codecentric.hc.gateway.routing;

import de.codecentric.hc.gateway.testing.WebTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static de.codecentric.hc.gateway.security.ApplicationUser.DEFAULT;
import static org.hamcrest.Matchers.hasItems;

public class TrackTest extends WebTest {

    @Test
    public void getTrackRecordsShouldReturnTrackRecordsForHabits() {
        get("/track/habits/123", DEFAULT).expectStatus().isOk()
                .expectBody().jsonPath("$[*]").value(hasItems("2019-03-19", "2019-03-20", "2019-03-21"));
    }

    @Test
    public void putTrackRecordsShouldUpsertTrackRecordsForHabits() {
        List<String> dates = Arrays.asList("2019-03-19", "2019-03-20", "2019-03-21");
        put("/track/habits/123", DEFAULT, dates).expectStatus().isOk();
    }
}
