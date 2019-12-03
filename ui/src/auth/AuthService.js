import { UserManager, WebStorageStateStore } from "oidc-client";
import { loadProtocol } from "./LoadProtocol";

let instance = null;

export default class AuthService {
  constructor() {
    if (instance) {
      return instance;
    }

    instance = this;

    this.settings = {
      userStore: new WebStorageStateStore(),
      authority: loadProtocol(process.env.VUE_APP_OIDC_AUTHORITY_HOST),
      client_id: process.env.VUE_APP_OIDC_CLIENT_ID,
      redirect_uri:
        window.location.origin + process.env.VUE_APP_UI_PATH + "/auth/callback",
      response_type: "code",
      scope: "openid profile address roles",
      post_logout_redirect_uri: window.location.origin + "/index.html",
      silent_redirect_uri: window.location.origin + "/auth/renew",
      accessTokenExpiringNotificationTime: 10,
      monitorSession: false,
      automaticSilentRenew: false,
      filterProtocolClaims: true,
      loadUserInfo: true
    };

    this._userManager = new UserManager(this.settings);
  }

  async getUser() {
    const user = await this._userManager.getUser();
    if (user) {
      return user;
    }

    return null;
  }

  async isSignedIn() {
    const user = await this._userManager.getUser();
    return user != null;
  }

  async getProfile() {
    const user = await this._userManager.getUser();

    if (user == null) {
      return null;
    }

    return user.profile;
  }

  async getIdToken() {
    const user = await this._userManager.getUser();

    if (user == null) {
      return null;
    }

    return user.id_token;
  }

  async getAccessToken() {
    const user = await this._userManager.getUser();

    if (user == null) {
      return null;
    }

    return user.access_token;
  }

  async isAccessTokenExpired() {
    const user = await this._userManager.getUser();

    if (user == null) {
      return true;
    }

    return user.expired;
  }

  async getSessionState() {
    const user = await this._userManager.getUser();

    if (user == null) {
      return null;
    }

    return user.session_state;
  }

  async getScopes() {
    const user = await this._userManager.getUser();

    if (user == null) {
      return null;
    }

    return user.scopes;
  }

  clearStaleState() {
    this._userManager.clearStaleState();
  }

  login() {
    return this._userManager.signinRedirect();
  }

  finalizeLogin() {
    return this._userManager.signinRedirectCallback();
  }

  renewToken() {
    return this._userManager.signinSilent();
  }

  finalizeRenewToken() {
    return this._userManager.signinSilentCallback();
  }

  logout() {
    this._userManager.signoutRedirect();
  }
}
