import useClearAuthQueryParams from "./useClearAuthQueryParams";
import { useSearchParams } from "react-router-dom";
import { Mock, vi } from "vitest";

vi.mock("react", () => {
  return {
    useEffect: (fn: () => void) => fn(),
  };
});

vi.mock("react-router-dom", () => {
  return {
    useSearchParams: vi.fn(),
  };
});

it("given authentication flow has been completed, search params include state and code should set search params to empty", () => {
  const setSearchParamsMock = vi.fn();
  (useSearchParams as Mock).mockReturnValue([
    new URLSearchParams("state=state&code=code"),
    setSearchParamsMock,
  ]);

  useClearAuthQueryParams(true);

  expect(setSearchParamsMock).toHaveBeenCalledWith({});
});

it("given authentication flow has not been completed should not set search params to empty", () => {
  const setSearchParamsMock = vi.fn();
  (useSearchParams as Mock).mockReturnValue([
    new URLSearchParams("state=state&code=code"),
    setSearchParamsMock,
  ]);

  useClearAuthQueryParams(false);

  expect(setSearchParamsMock).not.toHaveBeenCalledWith({});
});
