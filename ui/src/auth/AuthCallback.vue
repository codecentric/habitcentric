<script>
import AuthService from "./AuthService";

export default {
  name: "AuthCallback",
  data() {
    return {
      authService: new AuthService()
    };
  },
  async mounted() {
    try {
      await this.authService.finalizeLogin();
      this.$router.push({ path: "/habits" });
    } catch (error) {
      console.error(error);
    }
  }
};
</script>

<style scoped></style>
