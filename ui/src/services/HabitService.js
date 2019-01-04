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
}
