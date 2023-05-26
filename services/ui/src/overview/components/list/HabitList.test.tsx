import HabitList from "./HabitList";
import { screen, waitFor, waitForElementToBeRemoved, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import React from "react";
import { renderWithoutSwrCache } from "../../../test-utils/swr/RenderWithoutSwrCache";
import { trackedDatesMap } from "../../../test-utils/mocks/handlers";
import { format } from "date-fns";

it("renders list of four habits", async () => {
  renderWithoutSwrCache(<HabitList />);
  const list = await screen.findByRole("list", { name: /habits/i });
  const items = within(list).getAllByRole("listitem");
  expect(items.length).toEqual(4);
});

it("renders habit titles", async () => {
  renderWithoutSwrCache(<HabitList />);
  const items = await screen.findAllByRole("listitem");
  const titles = items.map((i) => within(i).getByRole("heading").textContent);
  expect(titles).toEqual(["Jogging", "Programming", "Working out", "Cooking"]);
});

it("renders habit schedule", async () => {
  renderWithoutSwrCache(<HabitList />);
  expect(await screen.findByText(/once per day/i)).toBeInTheDocument();
  expect(await screen.findByText(/twice per week/i)).toBeInTheDocument();
  expect(await screen.findByText(/6 times per month/i)).toBeInTheDocument();
  expect(await screen.findByText(/50 times per year/i)).toBeInTheDocument();
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

it("should delete habit when delete button is clicked", async () => {
  renderWithoutSwrCache(<HabitList />);
  const button = await screen.findByRole("button", {
    name: /delete programming habit/i,
  });
  await userEvent.click(button);
  const programming = screen.queryByRole("heading", { name: /programming/i });
  await waitFor(() => expect(programming).not.toBeInTheDocument());
  expect(programming).not.toBeInTheDocument();
});

it("filters habits by name based on search query", async () => {
  renderWithoutSwrCache(<HabitList />);

  const search = await screen.findByRole("searchbox");
  await userEvent.type(search, "jog");

  const items = screen.queryAllByRole("listitem");
  expect(items.length).toEqual(1);
  expect(within(items[0]).getByRole("heading", { name: "Jogging" })).toBeInTheDocument();
});

it("filters habits by schedule based on search query", async () => {
  renderWithoutSwrCache(<HabitList />);

  const search = await screen.findByRole("searchbox");
  await userEvent.type(search, "twice per week");

  const items = screen.queryAllByRole("listitem");
  expect(items.length).toEqual(1);
  expect(within(items[0]).getByRole("heading", { name: "Jogging" })).toBeInTheDocument();
});

it("should open datepicker when track button is clicked", async () => {
  renderWithoutSwrCache(<HabitList />);

  await clickJoggingTrackButton();
  const days = await screen.findAllByRole("option", {
    name: /Choose/i,
  });
  days.forEach((day) => {
    expect(day).toBeVisible();
  });
});

it("should highlight already tracked dates when datepicker is open", async () => {
  renderWithoutSwrCache(<HabitList />);

  await clickCookingTrackButton();
  const regexForTrackedDates = `${format(trackedDatesMap.get(4)![0], "EEEE, MMMM do, yyyy")}`;

  const alreadyTrackedDays = await screen.findAllByRole("option", {
    name: new RegExp(regexForTrackedDates, "i"),
  });
  for (const day of alreadyTrackedDays) {
    expect(day).toHaveClass("react-datepicker__day--highlighted");
  }
});

it("should highlight date when date is selected", async () => {
  renderWithoutSwrCache(<HabitList />);

  await clickWorkingOutTrackButton();
  let days = await screen.findAllByRole("option", {
    name: /Choose/i,
  });
  await userEvent.click(days[10]);

  await waitFor(() => expect(days[10]).toHaveClass("react-datepicker__day--highlighted"));
});

async function clickJoggingTrackButton() {
  const trackButton = await screen.findByRole("button", {
    name: /track jogging habit/i,
  });
  await userEvent.click(trackButton);
}

async function clickCookingTrackButton() {
  const trackButton = await screen.findByRole("button", {
    name: /track cooking habit/i,
  });
  await userEvent.click(trackButton);
}

async function clickWorkingOutTrackButton() {
  const trackButton = await screen.findByRole("button", {
    name: /track working out habit/i,
  });
  await userEvent.click(trackButton);
}
