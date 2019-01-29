import TrackingService from "@/tracking/TrackingService";
import MockAdapter from "axios-mock-adapter";
import axios from "axios";

describe("TrackingService", () => {
  const mock = new MockAdapter(axios);
  const service = new TrackingService();
  const userId = "default";
  const habitId = 123;
  const trackRecords = ["1999-12-31", "2000-01-01", "2000-01-02"];

  it("returns list of track records for habits", () => {
    mock
      .onGet(
        `http://dummy-tracking-service/track/users/${userId}/habits/${habitId}`
      )
      .reply(200, trackRecords);

    return expect(Promise.resolve(service.get(habitId))).resolves.toEqual(
      trackRecords
    );
  });

  it("updates track records for habits", () => {
    mock
      .onPut(
        `http://dummy-tracking-service/track/users/${userId}/habits/${habitId}`,
        trackRecords
      )
      .reply(200);

    return expect(
      Promise.resolve(service.put(habitId, trackRecords))
    ).resolves.toHaveProperty("status", 200);
  });
});
