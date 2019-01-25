<template>
  <div>
    <v-container v-if="error"> <DefaultErrorState /> </v-container>
    <div v-else>
      <LoadingSpinner v-if="loading" />
      <HabitList
        v-else
        v-bind:habits="habits"
        v-on:delete-habit="deleteHabit"
      />
      <v-spacer />
      <v-container v-if="errorWhileCreating || errorWhileDeleting">
        <DefaultErrorState />
      </v-container>
      <v-container>
        <HabitForm v-bind:habits="habits" v-on:create-habit="createHabit" />
      </v-container>
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
      errorWhileDeleting: null,
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
      this.errorWhileDeleting = undefined;
      this.loading = true;
      this.habitService
        .createHabit(name)
        .catch(error => {
          console.log(error);
          this.errorWhileCreating = error;
        })
        .finally(() => this.getHabits());
    },
    deleteHabit: async function(id) {
      this.errorWhileCreating = undefined;
      this.errorWhileDeleting = undefined;
      this.loading = true;
      this.habitService
        .deleteHabit(id)
        .catch(error => {
          console.log(error);
          this.errorWhileDeleting = error;
        })
        .finally(() => this.getHabits());
    }
  },
  mounted() {
    this.getHabits();
  }
};
</script>
