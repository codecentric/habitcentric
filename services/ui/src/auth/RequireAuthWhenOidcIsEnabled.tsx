import { useAuth } from "react-oidc-context";
import { Navigate, useLocation } from "react-router-dom";
import React from "react";
import useClearAuthQueryParams from "./useClearAuthQueryParams";
import { oidcIsEnabled } from "./config/oidc";

type RequireAuthWhenOidcIsEnabledProps = {
  children: JSX.Element;
};

function RequireAuthWhenOidcIsEnabled({
  children,
}: RequireAuthWhenOidcIsEnabledProps) {
  if (oidcIsEnabled()) {
    return <RequireAuth>{children}</RequireAuth>;
  } else {
    return children;
  }
}

function RequireAuth({ children }: { children: JSX.Element }) {
  let auth = useAuth();
  const loadingAuthContext = auth.isLoading && !auth.activeNavigator;
  useClearAuthQueryParams(!loadingAuthContext);
  let location = useLocation();

  if (loadingAuthContext) {
    return <span>Loading...</span>;
  }

  if (!auth.isAuthenticated) {
    return <Navigate to="/" state={{ from: location }} replace />;
  }

  return children;
}

export default RequireAuthWhenOidcIsEnabled;
