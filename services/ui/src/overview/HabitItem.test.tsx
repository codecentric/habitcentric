import { render, screen } from "@testing-library/react";
import { HabitItem } from "./HabitItem";

it("renders title", () => {
  render(
    <HabitItem
      name="Jogging"
      schedule={{ frequency: "WEEKLY", repetitions: 2 }}
    />
  );
  expect(screen.getByRole("heading", { name: "Jogging" })).toBeInTheDocument();
});

it("renders schedule as string", () => {
  render(
    <HabitItem
      name="Jogging"
      schedule={{ frequency: "WEEKLY", repetitions: 2 }}
    />
  );
  expect(screen.getByText("twice per week")).toBeInTheDocument();
});

it("renders track button", () => {
  render(
    <HabitItem
      name="Jogging"
      schedule={{ frequency: "WEEKLY", repetitions: 2 }}
    />
  );
  expect(screen.getByRole("button", { name: /track/i })).toBeInTheDocument();
});

it("renders delete button", () => {
  render(
    <HabitItem
      name="Jogging"
      schedule={{ frequency: "WEEKLY", repetitions: 2 }}
    />
  );
  expect(screen.getByRole("button", { name: /delete/i })).toBeInTheDocument();
});
