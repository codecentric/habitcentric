<template>
  <div>{{ achievementRates }}</div>
</template>

<script>
import ReportService from "../report/ReportService";

export default {
  name: "ReportContainer",
  data() {
    return {
      achievementRates: null,
      reportService: new ReportService(),
      loading: true
    };
  },
  methods: {
    getAchievementRates() {
      this.reportService
        .get()
        .catch(error => {
          console.log(error);
        })
        .then(achievementRates => {
          this.achievementRates = achievementRates;
        })
        .finally(() => (this.loading = false));
    }
  },
  created() {
    this.$eventHub.$on("habitsChanged", this.getAchievementRates);
  },
  beforeDestroy() {
    this.$eventHub.$off("habitsChanged");
  },
  mounted() {
    this.getAchievementRates();
  }
};
</script>
