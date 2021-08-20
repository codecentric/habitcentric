import axios from "axios/index";
import AuthService from "../auth/AuthService";

export class ApiService {
  constructor(host, oidcStatus = process.env.VUE_APP_OIDC_AUTH) {
    this.http = axios.create({
      baseURL: host,
      headers: {
        Accept: "application/json"
      }
    });

    if (!oidcStatus || oidcStatus === "disabled") {
      return;
    }

    this.authService = new AuthService();
    this.http.interceptors.request.use(
      async config => {
        const accessToken = await this.authService.getAccessToken();

        if (accessToken) {
          config.headers.Authorization = `Bearer ${accessToken}`;
        }

        return config;
      },
      async error => {
        throw Error(error);
      }
    );
  }
}
