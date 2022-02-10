import Footer from "./Footer";
import { render, screen } from "@testing-library/react";

it("renders link sections", () => {
  render(<Footer />);
  expect(screen.getByRole("heading", { name: /about/i })).toBeInTheDocument();
  expect(
    screen.getByRole("heading", { name: /contribute/i })
  ).toBeInTheDocument();
});
