import ReportService from "@/report/ReportService";
import MockAdapter from "axios-mock-adapter";
import axios from "axios";

describe("ReportService", () => {
  const mock = new MockAdapter(axios);
  const service = new ReportService();
  const achievementRates = {
    week: 0.5,
    month: 0.75
  };

  it("returns weekly and monthly achievement rate", () => {
    mock.onGet(`http://dummy-report-service/report/achievement`).reply(200, achievementRates);

    return expect(Promise.resolve(service.get())).resolves.toEqual(achievementRates);
  });
});
