import { http, HttpResponse } from "msw";
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
  http.get("/habits", async () => {
    return HttpResponse.json(habits);
  }),
  http.delete<{ habitId: string }>("/habits/:habitId", ({ params }) => {
    let { habitId } = params;
    habits = habits.filter((habit) => habit.id !== parseInt(habitId as string));
    trackedDatesMap.delete(parseInt(habitId as string));
    return HttpResponse.json(habits);
  }),
  http.get("/report/achievement", () => {
    return HttpResponse.json(achievement);
  }),
  http.post<never, CreateHabitRequest>("/habits", async ({ request }) => {
    const body = await request.json();
    const nextId = Math.max(...habits.map((habit) => habit.id)) + 1;
    habits = [
      ...habits,
      {
        id: nextId,
        ...body,
      },
    ];
    trackedDatesMap.set(nextId, []);
    return new Response(null, { status: 201 });
  }),
  http.get<{ habitId: string }>("/track/habits/:habitId", ({ params }) => {
    const { habitId } = params;
    return HttpResponse.json(trackedDatesMap.get(parseInt(habitId as string)));
  }),
  http.put<{ habitId: string }, TrackedDates>(
    "/track/habits/:habitId",
    async ({ request, params }) => {
      const { habitId } = params;
      const trackedDates = await request.json();
      trackedDatesMap.set(parseInt(habitId as string), trackedDates);
      return new Response(null, {
        status: 200,
      });
    }
  ),
];
