import Vue from "vue";
import Vuetify from "vuetify";
import HabitService from "@/services/HabitService";
import MockAdapter from "axios-mock-adapter";
import axios from "axios";

Vue.use(Vuetify);

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
    mock
      .onPost("http://dummy-habit-service/habits", { name: "new habit" })
      .reply(200);
    return expect(
      Promise.resolve(service.createHabit("new habit"))
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
