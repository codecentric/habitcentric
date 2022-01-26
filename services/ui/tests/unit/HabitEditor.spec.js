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
    expect(wrapper.text()).toContain("Name");
    expect(wrapper.text()).toContain("Repetitions");
    expect(wrapper.text()).toContain("Frequency");
    expect(wrapper.text()).toContain("Create habit");
  });

  it("can create habits", () => {
    expect(input.element.value).toEqual("");
    findNameInput().setValue("Jogging");
    expect(input.element.value).toEqual("Jogging");
    findAddButton().trigger("click");
    expect(wrapper.emitted()).toEqual({
      "create-habit": [
        [
          {
            name: "Jogging",
            schedule: {
              repetitions: 1,
              frequency: "DAILY"
            }
          }
        ]
      ]
    });
    expect(input.element.value).toEqual("");
  });

  it("can create habits having names with 64 characters", () => {
    expect(input.element.value).toEqual("");
    const value = "a".repeat(64);
    findNameInput().setValue(value);
    expect(input.element.value).toEqual(value);
    findAddButton().trigger("click");
    expect(wrapper.emitted()).toEqual({
      "create-habit": [
        [
          {
            name: value,
            schedule: { repetitions: 1, frequency: "DAILY" }
          }
        ]
      ]
    });
    expect(input.element.value).toEqual("");
  });

  it("prevents creating habits having names with more than 64 characters", () => {
    expect(input.element.value).toEqual("");
    const value = "a".repeat(65);
    findNameInput().setValue(value);
    expect(input.element.value).toEqual(value);
    findAddButton().trigger("click");
    expect(wrapper.text()).toContain("Name must be less than 64 characters");
    expect(wrapper.emitted()).toEqual({});
    expect(input.element.value).toEqual(value);
  });

  it("prevents creating habits without names", () => {
    expect(input.element.value).toEqual("");
    findNameInput().setValue("");
    expect(input.element.value).toEqual("");
    findAddButton().trigger("click");
    expect(wrapper.text()).toContain("Name is required");
    expect(wrapper.emitted()).toEqual({});
    expect(input.element.value).toEqual("");
  });

  it("prevents creating habits with duplicate names", () => {
    wrapper.setProps({ habits: [{ name: "Jogging" }, { name: "Meditate" }] });
    expect(input.element.value).toEqual("");
    findNameInput().setValue("Jogging");
    expect(input.element.value).toEqual("Jogging");
    findAddButton().trigger("click");
    expect(wrapper.text()).toContain("Name must be unique");
    expect(wrapper.emitted()).toEqual({});
    expect(input.element.value).toEqual("Jogging");
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
  const findAddButton = () => wrapper.find("button");
  const findClearIcon = () => wrapper.find("div.v-input__icon--clear .v-icon--link");
});
