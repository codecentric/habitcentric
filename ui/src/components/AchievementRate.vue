<template>
  <div class="achievement-rate-container">
    <v-progress-circular
      v-bind:value="percentage"
      size="80"
      width="7"
      color="blue"
    >
      {{ Math.round(percentage) }} %
    </v-progress-circular>
    <div class="achievement-rate-caption">{{ caption }}</div>
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
  text-align: center;
  margin: 10px 40px;
}

.achievement-rate-caption {
  margin-top: 10px;
}
</style>
