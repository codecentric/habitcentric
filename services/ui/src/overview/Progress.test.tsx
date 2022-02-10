import Progress from "./Progress";
import { render, screen } from "@testing-library/react";

it("renders title", () => {
  render(<Progress title="7 days" />);
  expect(screen.getByRole("heading")).toHaveTextContent("7 days");
});

it("renders percentage as text", () => {
  render(<Progress title="7 days" percentage={23} />);
  expect(screen.getByText("23%")).toBeInTheDocument();
});

it("renders percentage as visualization", () => {
  render(<Progress title="7 days" percentage={23} />);
  expect(screen.getByRole("progressbar")).toBeInTheDocument();
});
