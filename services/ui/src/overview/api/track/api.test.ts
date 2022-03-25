import { putTrackedDates } from "./api";

const mutatorMock = jest.fn();

describe("update tracked dates", () => {
  it("should mutate tracked dates state", async () => {
    await putTrackedDates(1, [new Date(), new Date(), new Date()], mutatorMock);
    expect(mutatorMock).toHaveBeenCalledWith(["/track/habits", 1]);
  });
});
