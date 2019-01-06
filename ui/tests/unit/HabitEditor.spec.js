import { mount } from "@vue/test-utils";
import HabitForm from "@/components/HabitForm";
import Vue from "vue";
import Vuetify from "vuetify";

Vue.use(Vuetify);

describe("HabitForm", () => {
  let wrapper;
  let input;

  beforeEach(() => {
    wrapper = mount(HabitForm);
    input = findNameInput();
  });

  it("renders", () => {
    expect(wrapper.text()).toMatch(
      "Create a new habit here0 / 64add_circle_outline"
    );
  });

  it("can create habits", () => {
    expect(input.element.value).toEqual("");
    findNameInput().setValue("Jogging");
    expect(input.element.value).toEqual("Jogging");
    findAddIcon().trigger("click");
    expect(wrapper.emitted()).toEqual({ "create-habit": [["Jogging"]] });
    expect(input.element.value).toEqual("");
  });

  it("allows clearing the name input", () => {
    expect(input.element.value).toEqual("");
    findNameInput().setValue("Jogging");
    expect(input.element.value).toEqual("Jogging");
    findClearIcon().trigger("click");
    expect(input.element.value).toEqual("");
    expect(wrapper.emitted()).toEqual({});
  });

  const findNameInput = () => wrapper.find('input[type="text"]');
  const findAddIcon = () =>
    wrapper.find("div.v-input__icon--append-outer .v-icon--link");
  const findClearIcon = () =>
    wrapper.find("div.v-input__icon--clear .v-icon--link");
});
