import Vue from "vue";
import Router from "vue-router";
import Habit from "./habits/details/Habit.vue";
import HabitListContainer from "./components/HabitListContainer";

Vue.use(Router);

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    { path: "/", redirect: "/habits" },
    {
      path: "/habits",
      component: HabitListContainer
    },
    {
      path: "/habits/:id",
      name: "habit",
      component: Habit
    },
    {
      path: "/about",
      name: "about",
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/About.vue")
    }
  ]
});
