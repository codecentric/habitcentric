import HabitList from "./HabitList";
import {
  screen,
  waitForElementToBeRemoved,
  within,
} from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import React from "react";
import { renderWithoutSwrCache } from "../../../test-utils/swr/RenderWithoutSwrCache";

it("renders loading message before showing habits", () => {
  renderWithoutSwrCache(<HabitList />);
  expect(screen.getByText("Loading...")).toBeVisible();
});

it("renders list of three habits", async () => {
  renderWithoutSwrCache(<HabitList />);
  const list = await screen.findByRole("list", { name: /habits/i });
  const items = within(list).getAllByRole("listitem");
  expect(items.length).toEqual(3);
});

it("renders habit titles", async () => {
  renderWithoutSwrCache(<HabitList />);
  const items = await screen.findAllByRole("listitem");
  const titles = items.map((i) => within(i).getByRole("heading").textContent);
  expect(titles).toEqual(["Jogging", "Programming", "Working out"]);
});

it("renders habit schedule", async () => {
  renderWithoutSwrCache(<HabitList />);
  expect(await screen.findByText(/once per day/i)).toBeInTheDocument();
  expect(await screen.findByText(/twice per week/i)).toBeInTheDocument();
  expect(await screen.findByText(/6 times per month/i)).toBeInTheDocument();
});

it("renders habit track buttons", async () => {
  renderWithoutSwrCache(<HabitList />);
  const buttons = await screen.findAllByRole("button", { name: /track/i });
  buttons.forEach((b) => expect(b).toBeInTheDocument());
});

it("renders habit delete buttons", async () => {
  renderWithoutSwrCache(<HabitList />);
  const buttons = await screen.findAllByRole("button", { name: /delete/i });
  buttons.forEach((b) => expect(b).toBeInTheDocument());
});

it("filters habits by name based on search query", async () => {
  renderWithoutSwrCache(<HabitList />);

  const search = await screen.findByRole("searchbox");
  await userEvent.type(search, "jog");

  const items = screen.queryAllByRole("listitem");
  expect(items.length).toEqual(1);
  expect(
    within(items[0]).getByRole("heading", { name: "Jogging" })
  ).toBeInTheDocument();
});

it("filters habits by schedule based on search query", async () => {
  renderWithoutSwrCache(<HabitList />);

  const search = await screen.findByRole("searchbox");
  await userEvent.type(search, "twice per week");

  const items = screen.queryAllByRole("listitem");
  expect(items.length).toEqual(1);
  expect(
    within(items[0]).getByRole("heading", { name: "Jogging" })
  ).toBeInTheDocument();
});

it("should delete habit when delete button is clicked", async () => {
  renderWithoutSwrCache(<HabitList />);
  const button = await screen.findByRole("button", {
    name: /delete jogging habit/i,
  });
  await userEvent.click(button);
  const jogging = screen.queryByRole("heading", { name: /jogging/i });
  await waitForElementToBeRemoved(jogging);
  expect(jogging).not.toBeInTheDocument();
});
