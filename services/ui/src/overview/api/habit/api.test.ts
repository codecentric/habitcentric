import { createHabit, deleteHabit } from "./api";

const mutatorMock = jest.fn();

describe("create habit", () => {
  it("should mutate habit state", async () => {
    await createHabit("Jogging", 2, "DAILY", mutatorMock);
    expect(mutatorMock).toHaveBeenCalledWith("/habits");
  });

  it("should mutate report achievement state", async () => {
    await createHabit("Jogging", 2, "DAILY", mutatorMock);
    expect(mutatorMock).toHaveBeenCalledWith("/report/achievement");
  });
});

describe("delete habit", () => {
  it("should mutate habit state", async () => {
    await deleteHabit(1, mutatorMock);
    expect(mutatorMock).toHaveBeenCalledWith("/habits");
  });

  it("should mutate report achievement state", async () => {
    await deleteHabit(1, mutatorMock);
    expect(mutatorMock).toHaveBeenCalledWith("/report/achievement");
  });
});
