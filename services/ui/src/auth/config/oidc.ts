export const oidc = {
  authority: "/auth/realms/habitcentric",
  client_id: "ui",
  redirect_uri: `${window.location.origin}${import.meta.env.BASE_URL}/overview`,
};

export function oidcIsEnabled() {
  return import.meta.env.VITE_OIDC_ENABLED?.toLowerCase() === "true";
}
