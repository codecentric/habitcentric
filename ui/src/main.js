import Vue from "vue";
import "./plugins/vuetify";
import App from "./App.vue";
import createRouter from "./router";
import store from "./store";
import "./filters/HabitScheduleFilter.js";

Vue.config.productionTip = false;

const router = createRouter();

Vue.prototype.$eventHub = new Vue();

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
