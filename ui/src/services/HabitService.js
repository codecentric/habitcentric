import { ApiService } from "../api/ApiService";

export default class HabitService extends ApiService {
  constructor(host = process.env.VUE_APP_HABIT_SERVICE_HOST) {
    super(host);
  }

  getHabits() {
    return this.http.get("habits").then(response => response.data);
  }

  createHabit(habit) {
    return this.http.post("habits", habit);
  }

  deleteHabit(id) {
    return this.http.delete(`habits/${id}`);
  }
}
