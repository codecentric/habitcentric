<template>
  <div>
    <v-list v-if="habits && habits.length > 0">
      <v-list-tile v-for="(habit, index) in habits" :key="index">
        <v-list-tile-content>
          <v-list-tile-title>{{ habit.name }}</v-list-tile-title>
          <v-list-tile-sub-title v-if="habit.schedule"
            >{{ habit.schedule | displaySchedule }}
          </v-list-tile-sub-title>
        </v-list-tile-content>
        <v-list-tile-action>
          <v-btn icon v-on:click="$emit('delete-habit', habit.id)">
            <v-icon>delete</v-icon>
          </v-btn>
        </v-list-tile-action>
        <v-list-tile-action>
          <v-btn icon :to="{ name: 'habit', params: habit }">
            <v-icon>more_horiz</v-icon>
          </v-btn>
        </v-list-tile-action>
      </v-list-tile>
    </v-list>
    <HabitListEmptyState v-else />
  </div>
</template>

<script>
import HabitListEmptyState from "./HabitListEmptyState";
export default {
  name: "HabitList",
  components: {
    HabitListEmptyState
  },
  props: {
    habits: Array
  }
};
</script>
