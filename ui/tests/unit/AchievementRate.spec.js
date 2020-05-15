import { mount } from "@vue/test-utils";
import Vue from "vue";
import AchievementRate from "../../src/components/AchievementRate";
import Vuetify from "vuetify";

Vue.use(Vuetify);

describe("AchievementRate", () => {
  const percentage = 13.37;

  it("renders circular progress", () => {
    const wrapper = mount(AchievementRate, { propsData: { percentage } });
    expect(wrapper.find({ name: "v-progress-circular" }).exists()).toBe(true);
  });

  it("renders percentage as integer", () => {
    const wrapper = mount(AchievementRate, { propsData: { percentage } });
    expect(wrapper.find({ name: "v-progress-circular" }).text()).toMatch(
      "13 %"
    );
  });
});
