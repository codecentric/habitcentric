import { shallowMount } from "@vue/test-utils";
import DefaultErrorState from "@/components/DefaultErrorState";
import Vue from "vue";
import Vuetify from "vuetify";

Vue.use(Vuetify);

describe("DefaultErrorState", () => {
  it("renders without habits", () => {
    const wrapper = shallowMount(DefaultErrorState);
    expect(wrapper.text()).toMatch(
      "An unexpected error occurred. Please try again later or contact your support\n" +
        "  team."
    );
  });
});
