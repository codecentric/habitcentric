<template>
  <div>
    <v-card>
      <v-card-title primary-title>
        <div>
          <div class="headline">
            <router-link :to="{ path: '/habits' }">Habits</router-link>
            <v-icon>chevron_right</v-icon>
            {{ this.$route.params.name }} -
            <span v-if="this.$route.params.schedule">{{
              this.$route.params.schedule | displaySchedule
            }}</span>
          </div>
        </div>
      </v-card-title>
    </v-card>
    <LoadingSpinner v-if="loading" />
    <v-container v-if="error"><DefaultErrorState /></v-container>
    <v-date-picker
      v-if="!loading && !error"
      v-model="dates"
      v-on:click:date="updateTrackReords"
      multiple
      no-title
      scrollable
      full-width
    >
    </v-date-picker>
  </div>
</template>

<script>
import DefaultErrorState from "@/components/DefaultErrorState";
import LoadingSpinner from "@/components/LoadingSpinner";
import TrackingService from "@/tracking/TrackingService";

export default {
  name: "Habit",
  components: { DefaultErrorState, LoadingSpinner },
  data: () => ({
    dates: [],
    error: null,
    loading: true,
    trackingService: new TrackingService()
  }),
  methods: {
    fetchTrackReords(habitId) {
      this.loading = true;
      this.trackingService
        .get(habitId)
        .then(dates => (this.dates = dates))
        .catch(e => this.handleError(e))
        .then(() => (this.loading = false));
    },
    updateTrackReords() {
      this.trackingService.put(this.$route.params.id, this.dates).catch(e => this.handleError(e));
    },
    handleError(error) {
      console.log(error);
      this.error = true;
    }
  },
  mounted() {
    this.fetchTrackReords(this.$route.params.id);
  }
};
</script>
