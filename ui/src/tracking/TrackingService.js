import axios from "axios";

export default class TrackingService {
  constructor() {
    this.http = axios.create({
      baseURL: process.env.VUE_APP_TRACKING_SERVICE_HOST
    });
  }

  get(habitId) {
    return this.http
      .get(`track/habits/${habitId}`)
      .then(response => response.data);
  }

  put(habitId, days) {
    return this.http.put(`track/habits/${habitId}`, days);
  }
}
