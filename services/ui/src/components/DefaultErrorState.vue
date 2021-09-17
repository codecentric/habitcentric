<template>
  <v-alert :value="true" type="error">
    An unexpected error occurred. Please try again later or contact your support
    team.
  </v-alert>
</template>

<script>
export default { name: "DefaultErrorState" };
</script>
