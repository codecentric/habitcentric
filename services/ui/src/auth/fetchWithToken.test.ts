import { fetchWithToken } from "./fetchWithToken";
import { SpyInstance, vi } from "vitest";

let fetchMock: SpyInstance;

beforeAll(() => {
  fetchMock = vi
    .spyOn(global, "fetch")
    .mockImplementation(() => Promise.resolve({ json: () => Promise.resolve({}) } as Response));
});

it("sets authorization header when token is passed", () => {
  fetchWithToken("url", {}, "token");
  expect(fetchMock).toHaveBeenCalledWith("url", {
    headers: {
      Authorization: "Bearer token",
    },
  });
});

it("does not set authorization header when token is not passed", () => {
  fetchWithToken("url", {});
  expect(fetchMock).toHaveBeenCalledWith("url", {
    headers: {},
  });
});

it("preserves headers when token is passed", () => {
  fetchWithToken("url", { headers: { "Content-Type": "application/json" } }, "token");
  expect(fetchMock).toHaveBeenCalledWith("url", {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer token",
    },
  });
});

it("preserves headers when token is not passed", () => {
  fetchWithToken("url", { headers: { "Content-Type": "application/json" } });
  expect(fetchMock).toHaveBeenCalledWith("url", {
    headers: {
      "Content-Type": "application/json",
    },
  });
});
