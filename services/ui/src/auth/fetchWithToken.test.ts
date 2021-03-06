import { fetchWithToken } from "./fetchWithToken";

global.fetch = jest.fn(() =>
  Promise.resolve({
    json: () => Promise.resolve({}),
  })
) as jest.Mock;

it("sets authorization header when token is passed", () => {
  fetchWithToken("url", {}, "token");
  expect(fetch).toHaveBeenCalledWith("url", {
    headers: {
      Authorization: "Bearer token",
    },
  });
});

it("does not set authorization header when token is not passed", () => {
  fetchWithToken("url", {});
  expect(fetch).toHaveBeenCalledWith("url", {
    headers: {},
  });
});

it("preserves headers when token is passed", () => {
  fetchWithToken(
    "url",
    { headers: { "Content-Type": "application/json" } },
    "token"
  );
  expect(fetch).toHaveBeenCalledWith("url", {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer token",
    },
  });
});

it("preserves headers when token is not passed", () => {
  fetchWithToken("url", { headers: { "Content-Type": "application/json" } });
  expect(fetch).toHaveBeenCalledWith("url", {
    headers: {
      "Content-Type": "application/json",
    },
  });
});
