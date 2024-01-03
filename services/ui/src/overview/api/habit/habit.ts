export type Habit = {
  id: string;
  name: string;
  schedule: Schedule;
};

export type Schedule = {
  frequency: Frequency;
  repetitions: number;
};

export type Frequency = "DAILY" | "WEEKLY" | "MONTHLY" | "YEARLY";

export function scheduleToString(schedule: Schedule): string {
  return `${repetitionsToString(schedule.repetitions)} per ${frequencyToString(
    schedule.frequency,
  )}`;
}

function repetitionsToString(repetitions: number): string {
  switch (repetitions) {
    case 1:
      return "once";
    case 2:
      return "twice";
    default:
      return `${repetitions} times`;
  }
}

function frequencyToString(frequency: Frequency): string {
  switch (frequency) {
    case "DAILY":
      return "day";
    case "WEEKLY":
      return "week";
    case "MONTHLY":
      return "month";
    case "YEARLY":
      return "year";
  }
}
