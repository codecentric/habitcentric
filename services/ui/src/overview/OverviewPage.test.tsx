import { render, screen } from "@testing-library/react";
import OverviewPage from "./OverviewPage";
import userEvent from "@testing-library/user-event";

it("should add habit when habit creation form is submitted", async () => {
  render(<OverviewPage/>);
  const nameField = screen.getByRole("textbox", { name: /name/i });
  const repetitionsField = screen.getByRole("spinbutton", { name: /repetitions/i });
  const frequencyField = screen.getByRole("combobox", { name: /frequency/i });
  const submitButton = screen.getByRole("button", { name: /create habit/i });

  await userEvent.type(nameField, "Skiing");
  await userEvent.type(repetitionsField, "5");
  await userEvent.selectOptions(frequencyField, "weekly");
  await userEvent.click(submitButton);

  const skiingHabit = await screen.findByRole("heading", { name: "Skiing" });
  expect(skiingHabit).toBeVisible();
});
