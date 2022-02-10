import { Frequency, scheduleToString } from "./habit";

it.each([
  ["once per day", "DAILY", 1],
  ["twice per week", "WEEKLY", 2],
  ["3 times per day", "DAILY", 3],
  ["4 times per week", "WEEKLY", 4],
  ["5 times per month", "MONTHLY", 5],
  ["6 times per year", "YEARLY", 6],
])(
  "returns '%s' when frequency is %s and repetitions is %s",
  (expectedResult: string, frequency: string, repetitions: number) => {
    const result = scheduleToString({
      frequency: frequency as Frequency,
      repetitions: repetitions,
    });
    expect(result).toBe(expectedResult);
  }
);
