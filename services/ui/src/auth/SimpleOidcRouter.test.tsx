import { render, screen } from "@testing-library/react";
import SimpleOidcRouter from "./SimpleOidcRouter";
import { AuthContextProps, useAuth } from "react-oidc-context";

jest.mock("react-oidc-context", () => {
  return {
    useAuth: jest.fn(),
  };
});

it("renders loading when authentication is still loading", () => {
  // @ts-ignore
  mockUseAuthReturnValue({
    isLoading: true,
  });

  render(
    <SimpleOidcRouter
      unauthenticatedPage={<span>unauthenticated</span>}
      authenticatedPage={<span>authenticated</span>}
    />
  );

  expect(screen.getByText("Loading...")).toBeVisible();
});

it("does not render loading when authentication signin redirect is in progress", () => {
  // @ts-ignore
  mockUseAuthReturnValue({
    activeNavigator: "signinRedirect",
  });

  render(
    <SimpleOidcRouter
      unauthenticatedPage={<span>unauthenticated</span>}
      authenticatedPage={<span>authenticated</span>}
    />
  );

  expect(screen.queryByText("Loading...")).not.toBeInTheDocument();
});

it("renders authenticated page when authentication is authenticated", () => {
  // @ts-ignore
  mockUseAuthReturnValue({
    isAuthenticated: true,
  });

  render(
    <SimpleOidcRouter
      unauthenticatedPage={<span>unauthenticated</span>}
      authenticatedPage={<span>authenticated</span>}
    />
  );

  expect(screen.getByText("authenticated")).toBeVisible();
});

it("renders unauthenticated page when authentication is unauthenticated", () => {
  // @ts-ignore
  mockUseAuthReturnValue({
    isAuthenticated: false,
  });

  render(
    <SimpleOidcRouter
      unauthenticatedPage={<span>unauthenticated</span>}
      authenticatedPage={<span>authenticated</span>}
    />
  );

  expect(screen.getByText("unauthenticated")).toBeVisible();
});

function mockUseAuthReturnValue(auth: AuthContextProps) {
  (useAuth as jest.Mock).mockReturnValue(auth);
}
