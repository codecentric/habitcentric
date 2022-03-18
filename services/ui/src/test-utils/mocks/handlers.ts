import { rest } from "msw";

const habits = [
  {
    id: 1,
    name: "Jogging",
    schedule: {
      frequency: "WEEKLY",
      repetitions: 2,
    },
  },
  {
    id: 2,
    name: "Programming",
    schedule: {
      frequency: "DAILY",
      repetitions: 1,
    },
  },
  {
    id: 3,
    name: "Working out",
    schedule: {
      frequency: "MONTHLY",
      repetitions: 6,
    },
  },
];

const achievement = {
  week: 50.0,
  month: 73.34,
};

export const handlers = [
  rest.get("/habits", (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(habits));
  }),
  rest.get("/report/achievement", (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(achievement));
  }),
];
