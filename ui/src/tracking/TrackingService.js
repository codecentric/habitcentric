import axios from "axios";

const userId = "default";

export default class TrackingService {
  constructor() {
    this.http = axios.create({
      baseURL: process.env.VUE_APP_TRACKING_SERVICE_HOST
    });
  }

  get(habitId) {
    return this.http
      .get(`track/users/${userId}/habits/${habitId}`)
      .then(response => response.data);
  }

  put(habitId, days) {
    return this.http.put(`track/users/${userId}/habits/${habitId}`, days);
  }
}
