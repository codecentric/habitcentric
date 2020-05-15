<template>
  <v-container v-if="achievementRates">
    <v-layout align-center justify-center row fill-height>
      <AchievementRate
        caption="Last 7 days"
        v-bind:percentage="achievementRates.week"
      />
      <AchievementRate
        caption="Last 30 days"
        v-bind:percentage="achievementRates.month"
      />
    </v-layout>
  </v-container>
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
