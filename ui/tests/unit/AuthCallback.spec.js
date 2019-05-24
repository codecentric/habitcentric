import { createLocalVue, shallowMount } from "@vue/test-utils";
import AuthCallback from "../../src/auth/AuthCallback";
import AuthService from "../../src/auth/AuthService";
import VueRouter from "vue-router";
jest.mock("../../src/auth/AuthService");

describe("AuthCallback", () => {
  let localVue;
  let routes;
  let router;

  const finalizeLoginMock = jest
    .fn()
    .mockImplementation(() => Promise.resolve());
  AuthService.mockImplementation(() => {
    return {
      finalizeLogin: finalizeLoginMock
    };
  });

  beforeEach(() => {
    localVue = createLocalVue();
    localVue.use(VueRouter);
    routes = [
      {
        path: "/habits"
      }
    ];
    router = new VueRouter({ routes });
  });

  afterEach(() => {
    finalizeLoginMock.mockClear();
  });

  describe("on mount", () => {
    let wrapper;

    beforeEach(() => {
      wrapper = shallowMount(AuthCallback, { localVue, router });
    });

    it("should finalize login with callback and route to habits", () => {
      expect(wrapper.vm.$route.path).toBe("/habits");
      expect(finalizeLoginMock).toHaveBeenCalledTimes(1);
    });
  });
});
