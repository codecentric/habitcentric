import { UserManager } from "oidc-client";
import AuthService from "../../../src/auth/AuthService";

jest.mock("oidc-client");

describe("AuthService", () => {
  const userManagerMock = {
    getUser: jest.fn(),
    clearStaleState: jest.fn(),
    signinRedirect: jest.fn(),
    signinRedirectCallback: jest.fn(),
    signinSilent: jest.fn(),
    signinSilentCallback: jest.fn(),
    signoutRedirect: jest.fn()
  };

  UserManager.mockImplementation(() => userManagerMock);
  const authService = new AuthService();

  afterEach(() => {
    jest.resetAllMocks();
  });

  describe("with saved user", () => {
    let mockedUser = {
      id_token: "token",
      profile: {
        role: "tester"
      },
      session_state: "session_state",
      access_token: "access_token",
      scope: "scope",
      expires_at: "expires_at",
      expires_in: "expires_in",
      expired: false,
      scopes: []
    };

    beforeEach(() => {
      userManagerMock.getUser.mockResolvedValue(mockedUser);
    });

    it("returns user", async () => {
      const user = await authService.getUser();
      expect(user).toEqual(mockedUser);
    });

    it("returns signed in as true", async () => {
      const signedIn = await authService.isSignedIn();
      expect(signedIn).toBeTruthy();
    });

    it("returns user profile", async () => {
      const profile = await authService.getProfile();
      expect(profile).toMatchObject(mockedUser.profile);
    });

    it("returns id token", async () => {
      const idToken = await authService.getIdToken();
      expect(idToken).toMatch(mockedUser.id_token);
    });

    it("returns access token", async () => {
      const accessToken = await authService.getAccessToken();
      expect(accessToken).toMatch(mockedUser.access_token);
    });

    it("returns access token expired", async () => {
      const expired = await authService.isAccessTokenExpired();
      expect(expired).toBeFalsy();
    });

    it("returns session state", async () => {
      const sessionState = await authService.getSessionState();
      expect(sessionState).toMatch(mockedUser.session_state);
    });

    it("returns scopes", async () => {
      const scopes = await authService.getScopes();
      expect(scopes).toMatchObject(mockedUser.scopes);
    });

    it("starts login flow", async () => {
      await authService.login();
      expect(userManagerMock.signinRedirect).toHaveBeenCalled();
    });

    it("finalizes login flow", async () => {
      await authService.finalizeLogin();
      expect(userManagerMock.signinRedirectCallback).toHaveBeenCalled();
    });

    it("starts token renew flow", async () => {
      await authService.renewToken();
      expect(userManagerMock.signinSilent).toHaveBeenCalled();
    });

    it("finalizes token renew flow", async () => {
      await authService.finalizeRenewToken();
      expect(userManagerMock.signinSilentCallback).toHaveBeenCalled();
    });

    it("starts logout flow", async () => {
      await authService.logout();
      expect(userManagerMock.signoutRedirect).toHaveBeenCalled();
    });
  });

  describe("without saved user", () => {
    it("returns undefined instead of user", async () => {
      const user = await authService.getUser();
      expect(user).toBeNull();
    });

    it("returns signed in as false", async () => {
      const signedIn = await authService.isSignedIn();
      expect(signedIn).toBeFalsy();
    });

    it("returns null instead of profile", async () => {
      const profile = await authService.getProfile();
      expect(profile).toBeNull();
    });

    it("returns null instead of id token", async () => {
      const idToken = await authService.getIdToken();
      expect(idToken).toBeNull();
    });

    it("returns null instead of access token", async () => {
      const accessToken = await authService.getAccessToken();
      expect(accessToken).toBeNull();
    });

    it("returns access token is expired", async () => {
      const expired = await authService.isAccessTokenExpired();
      expect(expired).toBeTruthy();
    });

    it("returns null instead of session state", async () => {
      const sessionState = await authService.getSessionState();
      expect(sessionState).toBeNull();
    });

    it("returns null instead of scopes", async () => {
      const scopes = await authService.getScopes();
      expect(scopes).toBeNull();
    });
  });
});
