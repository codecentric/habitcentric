import { createLocalVue } from "@vue/test-utils";
import { createRouter } from "../../src/router";
import AuthService from "../../src/auth/AuthService";

jest.mock("../../src/auth/AuthService");

describe("Router", () => {
  let localVue;
  let router;

  const isSignedInMock = jest.fn();
  const loginMock = jest.fn();
  const isAccessTokenExpiredMock = jest.fn();
  const renewTokenMock = jest.fn();
  AuthService.mockImplementation(() => {
    return {
      isSignedIn: isSignedInMock,
      login: loginMock,
      isAccessTokenExpired: isAccessTokenExpiredMock,
      renewToken: renewTokenMock
    };
  });

  beforeEach(() => {
    localVue = createLocalVue();
  });

  afterEach(() => {
    isSignedInMock.mockClear();
    loginMock.mockClear();
    isAccessTokenExpiredMock.mockClear();
    renewTokenMock.mockClear();
  });

  describe("with enabled OIDC", () => {
    beforeEach(() => {
      process.env.VUE_APP_OIDC_AUTH = "enabled";
      router = createRouter(localVue);
    });

    describe("on secured route", () => {
      let securedRoute = "/habits";

      describe("and anonymous user", () => {
        beforeEach(() => {
          isSignedInMock.mockImplementation(() => Promise.resolve(false));
        });

        it("starts OpenID connect login, aborts route", done => {
          router.push({ path: securedRoute }, undefined, () => {
            expect(loginMock).toHaveBeenCalledTimes(1);
            expect(router.currentRoute.fullPath).toBe("/");
            done();
          });
        });
      });

      describe("with authenticated user", () => {
        beforeEach(() => {
          isSignedInMock.mockImplementation(() => Promise.resolve(true));
        });

        describe("and invalid access and invalid refresh token", () => {
          beforeEach(() => {
            isAccessTokenExpiredMock.mockImplementation(() => {
              return true;
            });

            renewTokenMock.mockImplementation(() => {
              throw new Error();
            });
          });

          it("tries to renew token, but fails and starts OpenID connect login, aborts route", done => {
            router.push({ path: securedRoute }, undefined, () => {
              expect(renewTokenMock).toHaveBeenCalledTimes(1);
              expect(loginMock).toHaveBeenCalledTimes(1);
              expect(router.currentRoute.fullPath).toBe("/");
              done();
            });
          });
        });

        describe("and invalid access token and valid refresh token", () => {
          beforeEach(() => {
            isAccessTokenExpiredMock.mockImplementation(() => {
              return true;
            });
          });

          it("renews access token, aborts route", done => {
            router.push({ path: securedRoute }, undefined, () => {
              expect(router.currentRoute.fullPath).toBe("/");
              expect(renewTokenMock).toHaveBeenCalledTimes(1);
              done();
            });
          });
        });

        describe("and valid access token", () => {
          beforeEach(() => {
            isAccessTokenExpiredMock.mockImplementation(() => {
              return false;
            });
          });

          it("routes successfully", done => {
            router.push({ path: securedRoute }, () => {
              expect(router.currentRoute.fullPath).toBe(securedRoute);
              done();
            });
          });
        });
      });
    });

    describe("on auth callback route", () => {
      let authCallbackRoute = "/auth/callback";

      it("should route successfully", done => {
        router.push({ path: authCallbackRoute }, () => {
          expect(router.currentRoute.fullPath).toBe(authCallbackRoute);
          done();
        });
      });
    });
  });

  describe("with disabled OIDC", () => {
    beforeEach(() => {
      process.env.VUE_APP_OIDC_AUTH = "disabled";
      router = createRouter(localVue);
    });

    describe("on secured route", () => {
      let securedRoute = "/habits";

      it("routes successfully regardless of user or tokens", done => {
        router.push({ path: securedRoute }, () => {
          expect(isSignedInMock).toHaveBeenCalledTimes(0);
          expect(router.currentRoute.fullPath).toBe(securedRoute);
          done();
        });
      });
    });

    describe("on auth callback route", () => {
      let authCallbackRoute = "/auth/callback";

      it("should abort route", done => {
        router.push({ path: authCallbackRoute }, undefined, () => {
          expect(router.currentRoute.fullPath).toBe("/");
          done();
        });
      });
    });
  });
});
