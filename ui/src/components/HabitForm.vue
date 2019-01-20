<template>
  <v-form ref="form" @submit="submit">
    <v-text-field
      @click:append-outer="submit"
      append-outer-icon="add_circle_outline"
      clearable
      counter
      label="Create a new habit here"
      maxlength="64"
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
      name: ""
    };
  },
  methods: {
    submit(e) {
      e.preventDefault();
      this.$emit("create-habit", this.name);
      this.$refs.form.reset();
    }
  }
};
</script>
