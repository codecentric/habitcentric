import axios from "axios";

export default class HabitService {
  constructor(habitServiceHost = process.env.VUE_APP_HABIT_SERVICE_HOST) {
    this.http = axios.create({
      baseURL: habitServiceHost,
      headers: {
        Accept: "application/json"
      }
    });
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
