<template>
  <div class="achievement-rate-container">
    <div>{{ caption }}</div>
    <v-progress-circular
      v-bind:value="percentage"
      size="80"
      width="7"
      color="blue"
    >
      {{ Math.round(percentage) }} %
    </v-progress-circular>
  </div>
</template>

<script>
export default {
  name: "AchievementRate",
  props: {
    percentage: Number,
    caption: String
  }
};
</script>

<style>
.achievement-rate-container {
  margin: 10px 40px;
}
</style>
