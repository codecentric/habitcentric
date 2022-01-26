<template>
  <v-form ref="form" @submit.prevent="submit">
    <v-text-field
      :counter="64"
      :rules="nameRules"
      clearable
      label="Name"
      required
      type="text"
      v-model="name"
    ></v-text-field>
    <v-text-field
      :rules="repetitionsRules"
      label="Repetitions"
      min="1"
      type="number"
      v-model="repetitions"
    ></v-text-field>
    <v-select
      :items="frequencies"
      :rules="frequencyRules"
      item-text="text"
      item-value="value"
      label="Frequency"
      required
      v-model="frequency"
    ></v-select>
    <v-btn @click="submit" block color="primary">
      Create habit
      <v-icon right>add_circle_outline</v-icon>
    </v-btn>
  </v-form>
</template>

<script>
export default {
  name: "HabitForm",
  data() {
    return {
      frequencies: [
        { text: "daily", value: "DAILY" },
        { text: "weekly", value: "WEEKLY" },
        { text: "monthly", value: "MONTHLY" },
        { text: "yearly", value: "YEARLY" }
      ],
      frequency: "DAILY",
      frequencyRules: [v => !!v || "Frequency is required"],
      name: "",
      nameRules: [
        name => !!name || "Name is required",
        name => (name && name.length <= 64) || "Name must be less than 64 characters",
        name => (this.isDuplicateHabitName(name) ? "Name must be unique" : true)
      ],
      repetitions: 1,
      repetitionsRules: [
        repetitions => !!repetitions || "Repetitions is required",
        repetitions =>
          /^[1-9][0-9]*$/g.test(repetitions) || "Repetitions must be a positive integer"
      ]
    };
  },
  methods: {
    isDuplicateHabitName(name) {
      return name && this.habits && this.habits.some(habit => name === habit.name);
    },
    reset() {
      this.frequency = "DAILY";
      this.name = "";
      this.repetitions = 1;
      this.$refs.form.resetValidation();
    },
    submit() {
      if (this.$refs.form.validate()) {
        this.$emit("create-habit", {
          name: this.name,
          schedule: {
            repetitions: Number.parseInt(this.repetitions),
            frequency: this.frequency
          }
        });
        this.reset();
      }
    }
  },
  props: {
    habits: Array
  }
};
</script>
