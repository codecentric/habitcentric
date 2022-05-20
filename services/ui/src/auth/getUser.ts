import { User } from "oidc-client-ts";
import { oidc } from "./config/oidc";

export function getUser() {
  const oidcStorage = sessionStorage.getItem(`oidc.user:${oidc.authority}:ui`);
  if (!oidcStorage) {
    return null;
  }
  return User.fromStorageString(oidcStorage);
}
