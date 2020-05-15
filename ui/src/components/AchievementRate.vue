<template>
  <v-progress-circular
    v-bind:value="percentage"
    size="80"
    width="7"
    color="blue"
  >
    {{ Math.round(percentage) }} %
  </v-progress-circular>
</template>

<script>
export default {
  name: "AchievementRate",
  props: {
    percentage: Number
  }
};
</script>
