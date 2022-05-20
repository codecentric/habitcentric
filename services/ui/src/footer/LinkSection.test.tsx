import LinkSection from "./LinkSection";
import { render, screen } from "@testing-library/react";

it("renders title", () => {
  render(<LinkSection title="title" />);
  expect(screen.getByRole("heading")).toHaveTextContent("title");
});

it("renders links", () => {
  const links = [
    { text: "About", href: "http://example.org/about" },
    { text: "Company", href: "http://example.org/company" },
  ];
  render(<LinkSection title={"title"} links={links} />);
  expect(screen.getByRole("link", { name: /about/i })).toHaveAttribute(
    "href",
    "http://example.org/about"
  );
  expect(screen.getByRole("link", { name: /company/i })).toHaveAttribute(
    "href",
    "http://example.org/company"
  );
});
