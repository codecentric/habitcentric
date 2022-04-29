export const oidc = {
  authority: "/auth/realms/habitcentric",
  client_id: "ui",
  redirect_uri: `${window.location.origin}${process.env.PUBLIC_URL}/overview`,
};

export function oidcIsEnabled() {
  return process.env.REACT_APP_OIDC_ENABLED?.toLowerCase() === "true";
}
