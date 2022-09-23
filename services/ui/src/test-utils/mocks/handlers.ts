import { rest } from "msw";
import { sub } from "date-fns";
import { CreateHabitRequest } from "../../overview/api/habit/api";
import { TrackedDates } from "../../overview/api/track/model";

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
  {
    id: 4,
    name: "Cooking",
    schedule: {
      frequency: "YEARLY",
      repetitions: 50,
    },
  },
];

function todayMinusDays(days: number): Date {
  return sub(new Date(), { days: days });
}

export const trackedDatesMap = new Map<number, TrackedDates>([
  [1, [todayMinusDays(1), todayMinusDays(2)]],
  [2, [todayMinusDays(3), todayMinusDays(4)]],
  [3, []],
  [4, [todayMinusDays(0)]],
]);

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
    trackedDatesMap.delete(parseInt(habitId as string));
    return res(ctx.status(200), ctx.json(habits));
  }),
  rest.get("/report/achievement", (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(achievement));
  }),
  rest.post<CreateHabitRequest>("/habits", (req, res, ctx) => {
    const body = req.body;
    const nextId = Math.max(...habits.map((habit) => habit.id)) + 1;
    habits = [
      ...habits,
      {
        id: nextId,
        ...body,
      },
    ];
    trackedDatesMap.set(nextId, []);
    return res(ctx.status(201));
  }),
  rest.get("/track/habits/:habitId", (req, res, ctx) => {
    const { habitId } = req.params;
    return res(
      ctx.status(200),
      ctx.json(trackedDatesMap.get(parseInt(habitId as string)))
    );
  }),
  rest.put<TrackedDates>("/track/habits/:habitId", (req, res, ctx) => {
    const { habitId } = req.params;
    const trackedDates = req.body;
    trackedDatesMap.set(parseInt(habitId as string), trackedDates);
    return res(ctx.status(200));
  }),
];
