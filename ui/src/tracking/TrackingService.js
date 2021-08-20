import { ApiService } from "../api/ApiService";

export default class TrackingService extends ApiService {
  constructor(host = process.env.VUE_APP_TRACKING_SERVICE_HOST) {
    super(host);
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
