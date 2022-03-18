import Report from "./Report";
import { screen } from "@testing-library/react";
import { renderWithoutSwrCache } from "../../../test-utils/swr/RenderWithoutSwrCache";

it("renders loading message before showing percentages", () => {
  renderWithoutSwrCache(<Report />);
  expect(screen.getByText("Loading...")).toBeVisible();
});

it("renders week percentage as text", async () => {
  renderWithoutSwrCache(<Report />);
  const percentage = await screen.findByText("50%");
  expect(percentage).toBeInTheDocument();
});

it("renders month percentage as text", async () => {
  renderWithoutSwrCache(<Report />);
  const percentage = await screen.findByText("73.34%");
  expect(percentage).toBeInTheDocument();
});

it("renders week percentage as visualization", async () => {
  renderWithoutSwrCache(<Report />);
  const progressbar = await screen.findByRole("progressbar", {
    name: "7 days progress",
  });
  expect(progressbar).toHaveStyle("width: 50%");
});

it("renders month percentage as visualization", async () => {
  renderWithoutSwrCache(<Report />);
  const progressbar = await screen.findByRole("progressbar", {
    name: "30 days progress",
  });
  expect(progressbar).toHaveStyle("width: 73.34%");
});
