import axios from "axios";

export default class HabitService {
  constructor() {
    this.http = axios.create({
      baseURL: process.env.VUE_APP_HABIT_SERVICE_HOST
    });
  }

  getHabits() {
    return this.http.get("habits").then(response => response.data);
  }

  createHabit(name) {
    return this.http.post("habits", { name: name });
  }

  deleteHabit(id) {
    return this.http.delete(`habits/${id}`);
  }
}
