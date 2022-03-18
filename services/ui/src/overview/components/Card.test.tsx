import Card from "./Card";
import { render, screen } from "@testing-library/react";

it("renders title", () => {
  render(<Card title="Card title" />);
  expect(screen.getByRole("heading")).toHaveTextContent("Card title");
});
