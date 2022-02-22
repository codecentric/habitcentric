import HabitList from "./HabitList";
import { render, screen, within } from "@testing-library/react";
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
  {
    id: 3,
    name: "Working out",
    schedule: {
      frequency: "MONTHLY",
      repetitions: 3,
    },
  },
];

it("renders list of three habits", () => {
  render(<HabitList habits={habits} />);
  const list = screen.getByRole("list", { name: /habits/i });
  const items = within(list).getAllByRole("listitem");
  expect(items.length).toEqual(3);
});

it("renders habit titles", () => {
  render(<HabitList habits={habits} />);
  const items = screen.getAllByRole("listitem");
  const titles = items.map((i) => within(i).getByRole("heading").textContent);
  expect(titles).toEqual(["Jogging", "Programming", "Working out"]);
});

it("renders habit schedule", () => {
  render(<HabitList habits={habits} />);
  expect(screen.getByText(/once per day/i)).toBeInTheDocument();
  expect(screen.getByText(/twice per week/i)).toBeInTheDocument();
  expect(screen.getByText(/3 times per month/i)).toBeInTheDocument();
});

it("renders habit track buttons", () => {
  render(<HabitList habits={habits} />);
  const buttons = screen.getAllByRole("button", { name: /track/i });
  buttons.forEach((b) => expect(b).toBeInTheDocument());
});

it("renders habit delete buttons", () => {
  render(<HabitList habits={habits} />);
  const buttons = screen.getAllByRole("button", { name: /delete/i });
  buttons.forEach((b) => expect(b).toBeInTheDocument());
});
