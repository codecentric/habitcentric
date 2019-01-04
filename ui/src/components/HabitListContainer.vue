<template>
  <div>
    <v-container v-if="error"> <DefaultErrorState /> </v-container>
    <div v-else>
      <LoadingSpinner v-if="loading" />
      <HabitList v-else v-bind:habits="habits" />
    </div>
  </div>
</template>

<script>
import DefaultErrorState from "@/components/DefaultErrorState";
import HabitList from "@/components/HabitList";
import HabitService from "@/services/HabitService";
import LoadingSpinner from "@/components/LoadingSpinner";

export default {
  name: "HabitListContainer",
  components: {
    LoadingSpinner,
    DefaultErrorState,
    HabitList
  },
  data() {
    return {
      error: null,
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
    }
  },
  mounted() {
    this.getHabits();
  }
};
</script>
