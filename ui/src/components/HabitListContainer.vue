<template>
  <div>
    <v-container v-if="error"> <DefaultErrorState /> </v-container>
    <div v-else>
      <LoadingSpinner v-if="loading" />
      <HabitList v-else v-bind:habits="habits" />
      <v-spacer />
      <v-container v-if="errorWhileCreating">
        <DefaultErrorState />
      </v-container>
      <v-container> <HabitForm v-on:create-habit="createHabit" /> </v-container>
    </div>
  </div>
</template>

<script>
import DefaultErrorState from "@/components/DefaultErrorState";
import HabitForm from "@/components/HabitForm";
import HabitList from "@/components/HabitList";
import HabitService from "@/services/HabitService";
import LoadingSpinner from "@/components/LoadingSpinner";

export default {
  name: "HabitListContainer",
  components: {
    LoadingSpinner,
    DefaultErrorState,
    HabitForm,
    HabitList
  },
  data() {
    return {
      error: null,
      errorWhileCreating: null,
      habits: null,
      habitService: new HabitService(),
      loading: true
    };
  },
  methods: {
    getHabits() {
      this.error = undefined;
      this.habitService
        .getHabits()
        .catch(error => {
          console.log(error);
          this.error = error;
        })
        .then(habits => {
          this.habits = habits;
        })
        .finally(() => (this.loading = false));
    },
    createHabit(name) {
      this.errorWhileCreating = undefined;
      this.loading = true;
      this.habitService
        .createHabit(name)
        .catch(error => {
          console.log(error);
          this.errorWhileCreating = error;
        })
        .finally(() => this.getHabits());
    }
  },
  mounted() {
    this.getHabits();
  }
};
</script>
