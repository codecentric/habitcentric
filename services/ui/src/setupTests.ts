// jest-dom adds custom jest matchers for asserting on DOM nodes.
// allows you to do things like:
// expect(element).toHaveTextContent(/react/i)
// learn more: https://github.com/testing-library/jest-dom
import "@testing-library/jest-dom";
import { server } from "./test-utils/mocks/server";
import { vi } from "vitest";
import { siOrg } from "simple-icons";

vi.mock("./auth/getUser");
vi.mock("react-oidc-context", () => ({
  useAuth: () => {
    return {
      user: {
        access_token: "access_token",
      },
    };
  },
}));

// Small url "polyfill" to make urls work on nodejs with vitest
// jest brings its own polyfill but vitest does not. But our test rely on relative paths
// during api calls...
const originalUrl = URL;
const myUrl = function (url: string | URL, base?: string | URL | undefined) {
  return new originalUrl(url, base || "http://localhost/");
};
//@ts-ignore
URL = myUrl;

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());
