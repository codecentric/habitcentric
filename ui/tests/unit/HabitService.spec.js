import HabitService from "@/services/HabitService";
import MockAdapter from "axios-mock-adapter";
import axios from "axios";

describe("HabitService", () => {
  const mock = new MockAdapter(axios);
  const service = new HabitService();

  it("returns list of habits", () => {
    const habits = [
      { name: "Jogging" },
      { name: "Meditate" },
      { name: "Play guitar" }
    ];

    mock.onGet("http://dummy-habit-service/habits").reply(200, habits);

    return expect(Promise.resolve(service.getHabits())).resolves.toEqual(
      habits
    );
  });

  it("create habits", () => {
    const habit = { name: "new habit", repetitions: 1, frequency: "DAILY" };
    mock.onPost("http://dummy-habit-service/habits", habit).reply(200);
    return expect(
      Promise.resolve(service.createHabit(habit))
    ).resolves.toMatchObject({
      status: 200
    });
  });

  it("deletes habits", () => {
    mock.onDelete("http://dummy-habit-service/habits/123").reply(200);
    return expect(
      Promise.resolve(service.deleteHabit(123))
    ).resolves.toMatchObject({
      status: 200
    });
  });
});
