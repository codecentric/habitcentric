import useClearAuthQueryParams from "./useClearAuthQueryParams";
import { useSearchParams } from "react-router-dom";

jest.mock("react", () => {
  return {
    useEffect: (fn: () => void) => fn(),
  };
});

jest.mock("react-router-dom", () => {
  return {
    useSearchParams: jest.fn(),
  };
});

it("given authentication flow has been completed, search params include state and code should set search params to empty", () => {
  const setSearchParamsMock = jest.fn();
  (useSearchParams as jest.Mock).mockReturnValue([
    new URLSearchParams("state=state&code=code"),
    setSearchParamsMock,
  ]);

  useClearAuthQueryParams(true);

  expect(setSearchParamsMock).toHaveBeenCalledWith({});
});

it("given authentication flow has not been completed should not set search params to empty", () => {
  const setSearchParamsMock = jest.fn();
  (useSearchParams as jest.Mock).mockReturnValue([
    new URLSearchParams("state=state&code=code"),
    setSearchParamsMock,
  ]);

  useClearAuthQueryParams(false);

  expect(setSearchParamsMock).not.toHaveBeenCalledWith({});
});
