import { rest } from "msw";

let habits = [
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
  week: 50.67,
  month: 73.34,
};

export const handlers = [
  rest.get("/habits", (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(habits));
  }),
  rest.delete("/habits/:habitId", (req, res, ctx) => {
    let { habitId } = req.params;
    habits = habits.filter((habit) => habit.id !== parseInt(habitId as string));
    return res(ctx.status(200), ctx.json(habits));
  }),
  rest.get("/report/achievement", (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(achievement));
  }),
  rest.post("/habits", (req, res, ctx) => {
    const body = JSON.parse(req.body as string);
    habits = [...habits, {
      id: Math.max(...habits.map(habit => habit.id)) + 1,
      ...body
    }];
    return res(ctx.status(201));
  })
];
