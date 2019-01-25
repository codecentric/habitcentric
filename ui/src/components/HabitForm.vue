<template>
  <v-form ref="form" @submit.prevent="submit">
    <v-text-field
      @click:append-outer="submit"
      @click:clear="$refs.form.reset()"
      v-on:blur="$refs.form.resetValidation()"
      :counter="64"
      :rules="nameRules"
      append-outer-icon="add_circle_outline"
      clearable
      label="Create a new habit here"
      required
      type="text"
      v-model="name"
    ></v-text-field>
  </v-form>
</template>

<script>
export default {
  name: "HabitForm",
  data() {
    return {
      name: "",
      nameRules: [
        name => !!name || "Name is required",
        name =>
          (name && name.length <= 64) || "Name must be less than 64 characters",
        name => (this.isDuplicateHabitName(name) ? "Name must be unique" : true)
      ]
    };
  },
  methods: {
    isDuplicateHabitName(name) {
      return (
        name && this.habits && this.habits.some(habit => name === habit.name)
      );
    },
    submit() {
      if (this.$refs.form.validate()) {
        this.$emit("create-habit", this.name);
        this.$refs.form.reset();
      }
    }
  },
  props: {
    habits: Array
  }
};
</script>
