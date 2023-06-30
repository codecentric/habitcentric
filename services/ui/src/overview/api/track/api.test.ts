import { putTrackedDates } from "./api";
import { vi } from "vitest";

const mutatorMock = vi.fn();
vi.mock("../../../auth/getUser");

describe("update tracked dates", () => {
  it("should mutate tracked dates state", async () => {
    await putTrackedDates(1, [new Date(), new Date(), new Date()], mutatorMock);
    expect(mutatorMock).toHaveBeenCalledWith(["/track/habits", 1, "access_token"]);
  });

  it("should mutate achievement report state", async () => {
    await putTrackedDates(1, [new Date(), new Date(), new Date()], mutatorMock);
    expect(mutatorMock).toHaveBeenCalledWith(["/report/achievement", "access_token"]);
  });
});
