import { render, screen } from "@testing-library/react";
import Header from "./Header";

it("renders codecentric logo", () => {
  render(<Header />);
  expect(screen.getByAltText("codecentric logo")).toBeInTheDocument();
});

it("renders header text", () => {
  render(<Header />);
  expect(screen.getByText("habitcentric")).toBeInTheDocument();
});

it("renders github link", () => {
  render(<Header />);
  expect(screen.getByTitle("GitHub repository")).toBeInTheDocument();
});
