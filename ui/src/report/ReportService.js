import { ApiService } from "../api/ApiService";

export default class ReportService extends ApiService {
  constructor(host = process.env.VUE_APP_REPORT_SERVICE_HOST) {
    super(host);
  }

  get() {
    return this.http.get(`report/achievement`).then(response => response.data);
  }
}
