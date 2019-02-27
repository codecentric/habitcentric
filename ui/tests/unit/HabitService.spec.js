import HabitService from "@/services/HabitService";
import axios from "axios";
import httpAdapter from "axios/lib/adapters/http";
import path from "path";
import { Pact } from "@pact-foundation/pact";
import { like } from "@pact-foundation/pact/dsl/matchers";

describe("HabitService", () => {
  axios.defaults.adapter = httpAdapter; // to enable CORS

  const service = new HabitService("http://localhost:8123/");

  const provider = new Pact({
    consumer: "hc-ui",
    provider: "hc-habit",
    dir: path.resolve(process.cwd(), "pacts"),
    log: path.resolve(process.cwd(), "logs", "pact.log"),
    logLevel: "INFO",
    port: 8123,
    spec: 2
  });

  beforeAll(async () => await provider.setup());
  afterEach(async () => await provider.verify());
  afterAll(async () => await provider.finalize());

  it("returns list of habits", async () => {
    const habits = [
      {
        id: like(1),
        name: "Jogging",
        schedule: { repetitions: 2, frequency: "WEEKLY" }
      },
      {
        id: like(101),
        name: "Meditate",
        schedule: { repetitions: 1, frequency: "DAILY" }
      },
      {
        id: like(51),
        name: "Play guitar",
        schedule: { repetitions: 5, frequency: "MONTHLY" }
      }
    ];

    await provider.addInteraction({
      state: "habits 'Jogging', 'Meditate' and 'Play guitar' exist",
      uponReceiving: "request to return the habits of a user",
      withRequest: {
        method: "GET",
        path: "/habits",
        headers: {
          Accept: "application/json"
        }
      },
      willRespondWith: {
        status: 200,
        headers: {
          "Content-Type": "application/json"
        },
        body: habits
      }
    });

    return expect(Promise.resolve(service.getHabits())).resolves.toEqual([
      {
        id: 1,
        name: "Jogging",
        schedule: { repetitions: 2, frequency: "WEEKLY" }
      },
      {
        id: 101,
        name: "Meditate",
        schedule: { repetitions: 1, frequency: "DAILY" }
      },
      {
        id: 51,
        name: "Play guitar",
        schedule: { repetitions: 5, frequency: "MONTHLY" }
      }
    ]);
  });

  it("create habits", async () => {
    const habit = {
      name: "new habit",
      schedule: { repetitions: 1, frequency: "DAILY" }
    };

    await provider.addInteraction({
      uponReceiving: "request to create a new habit",
      withRequest: {
        method: "POST",
        path: "/habits",
        headers: {
          Accept: "application/json"
        },
        body: habit
      },
      willRespondWith: {
        status: 201
      }
    });

    return expect(
      Promise.resolve(service.createHabit(habit))
    ).resolves.toMatchObject({ status: 201 });
  });

  it("deletes habits", async () => {
    await provider.addInteraction({
      state: "habit with id '123' exists",
      uponReceiving: "request to delete the habit with id '123'",
      withRequest: {
        method: "DELETE",
        path: "/habits/123"
      },
      willRespondWith: {
        status: 200
      }
    });

    return expect(
      Promise.resolve(service.deleteHabit(123))
    ).resolves.toMatchObject({ status: 200 });
  });
});
