<template>
  <div>
    <AchievementRate
      caption="Last 7 days"
      v-bind:percentage="achievementRates.week"
    />
    <AchievementRate
      caption="Last 30 days"
      v-bind:percentage="achievementRates.month"
    />
  </div>
</template>

<script>
import ReportService from "../report/ReportService";
import AchievementRate from "@/components/AchievementRate";

export default {
  name: "ReportContainer",
  components: {
    AchievementRate
  },
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
