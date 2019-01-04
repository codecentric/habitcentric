import { mount } from "@vue/test-utils";
import HabitList from "@/components/HabitList";
import Vue from "vue";
import Vuetify from "vuetify";

Vue.use(Vuetify);

describe("HabitList", () => {
  const habits = [
    { id: 1, name: "Jogging" },
    { id: 101, name: "Meditate" },
    { id: 51, name: "Play guitar" }
  ];

  it("renders with habits", () => {
    const wrapper = mount(HabitList, { propsData: { habits } });
    expect(wrapper.text()).toMatch("JoggingMeditatePlay guitar");
  });

  it("renders with an empty array of habits", () => {
    const habits = [];
    const wrapper = mount(HabitList, {
      propsData: { habits }
    });
    expect(wrapper.text()).toMatch("You haven't created any habits yet.");
  });

  it("renders without habits", () => {
    const wrapper = mount(HabitList);
    expect(wrapper.text()).toMatch("You haven't created any habits yet.");
  });
});
