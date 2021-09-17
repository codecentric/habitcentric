<template>
  <v-container v-if="achievementRates">
    <v-layout column align-center>
      <div class="achievement-rate-">Historic Achievement</div>
      <v-layout align-center justify-center row fill-height>
        <AchievementRate
          caption="Last 7 days"
          v-if="achievementRates.week"
          v-bind:percentage="achievementRates.week"
        />
        <AchievementRate
          caption="Last 30 days"
          v-if="achievementRates.month"
          v-bind:percentage="achievementRates.month"
        />
      </v-layout>
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
        .catch(() => {
          // Report service is not essential for habitcentric
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
