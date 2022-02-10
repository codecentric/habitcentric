import HabitList from "./HabitList";
import { render, screen } from "@testing-library/react";
import { Habit } from "./habit";

const habits: Array<Habit> = [
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
];

it("renders habits", () => {
  render(<HabitList habits={habits} />);
  expect(screen.getByRole("heading", { name: /jogging/i })).toBeInTheDocument();
  expect(
    screen.getByRole("heading", { name: /programming/i })
  ).toBeInTheDocument();
});
