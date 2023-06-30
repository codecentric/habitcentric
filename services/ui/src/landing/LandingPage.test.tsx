import { render, screen } from "@testing-library/react";
import LandingPage from "./LandingPage";
import { oidcIsEnabled } from "../auth/config/oidc";
import userEvent from "@testing-library/user-event";
import { useAuth } from "react-oidc-context";
import { Mock, vi } from "vitest";

vi.mock("../auth/config/oidc");
vi.mock("react-oidc-context", () => {
  return {
    useAuth: vi.fn(),
  };
});

describe("given oidc is enabled", () => {
  beforeEach(() => {
    (oidcIsEnabled as Mock).mockReturnValue(true);
  });

  it("starts oidc flow when user clicks on login button", async () => {
    const signinRedirect = vi.fn();
    signinRedirect.mockResolvedValue({});
    (useAuth as Mock).mockReturnValue({ signinRedirect });

    render(<LandingPage />);
    const loginButton = screen.getByRole("button", { name: /log in/i });
    await userEvent.click(loginButton);

    expect(signinRedirect).toHaveBeenCalled();
  });

  it("renders error when user clicks on login button but oidc flow start fails", async () => {
    const signinRedirect: any = vi.fn();
    signinRedirect.mockRejectedValue({});
    (useAuth as Mock).mockReturnValue({ signinRedirect });

    render(<LandingPage />);

    const loginButton = screen.getByRole("button", { name: /log in/i });
    await userEvent.click(loginButton);

    const error = screen.getByText("Oops... Something went wrong!");
    expect(error).toBeVisible();
  });

  it("renders error when auth error occured", () => {
    (useAuth as Mock).mockReturnValue({ error: "Some error" });
    render(<LandingPage />);
    const error = screen.getByText("Oops... Something went wrong!");
    expect(error).toBeVisible();
  });
});

describe("given oidc is disabled", () => {
  beforeEach(() => {
    (oidcIsEnabled as Mock).mockReturnValue(false);
    (useAuth as Mock).mockReturnValue({});
  });

  it("renders link to overview", async () => {
    render(<LandingPage />);

    const loginLink = screen.getByRole("link", { name: /log in/i });
    expect(loginLink).toBeVisible();
  });
});
