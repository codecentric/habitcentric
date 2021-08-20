import Vue from "vue";
import Router from "vue-router";
import Habit from "./habits/details/Habit.vue";
import HabitListContainer from "./components/HabitListContainer";
import AuthCallback from "./auth/AuthCallback";
import AuthService from "./auth/AuthService";

const routes = [
  { path: "/", redirect: "/habits" },
  {
    path: "/habits",
    component: HabitListContainer,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/habits/:id",
    name: "habit",
    component: Habit,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/auth/callback",
    name: "auth-callback",
    component: AuthCallback,
    beforeEnter: (to, from, next) => {
      if (_oidcIsEnabled()) {
        next();
      } else {
        next(false);
      }
    }
  },
  {
    path: "/about",
    name: "about",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ "./views/About.vue")
  }
];

export function createRouter(
  vueInstance = Vue,
  authService = _oidcIsEnabled() ? new AuthService() : null
) {
  vueInstance.use(Router);

  const router = new Router({
    mode: "history",
    base: process.env.BASE_URL,
    routes: routes
  });

  router.beforeEach(async (to, from, next) => {
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);

    if (requiresAuth && _oidcIsEnabled()) {
      next(await _isAuthorized());
    } else {
      next();
    }
  });

  async function _isAuthorized() {
    const signedIn = await authService.isSignedIn();
    if (!signedIn) {
      await authService.login();
      return false;
    }

    const accessTokenExpired = await authService.isAccessTokenExpired();
    if (!accessTokenExpired) {
      return true;
    }

    try {
      await authService.renewToken();
      return true;
    } catch (error) {
      await authService.login();
      return false;
    }
  }

  return router;
}

function _oidcIsEnabled() {
  return (
    JSON.stringify(process.env.VUE_APP_OIDC_AUTH) === JSON.stringify("enabled")
  );
}

export default createRouter;
