import { useAuth } from "react-oidc-context";
import React, { ReactNode } from "react";

type SimpleOidcRouterProps = {
  unauthenticatedPage: ReactNode;
  authenticatedPage: ReactNode;
};

function SimpleOidcRouter({
  unauthenticatedPage,
  authenticatedPage,
}: SimpleOidcRouterProps) {
  const auth = useAuth();

  if (auth.isLoading && !auth.activeNavigator) {
    return <span>Loading...</span>;
  }

  if (auth.isAuthenticated) {
    return <>{authenticatedPage}</>;
  }

  return <>{unauthenticatedPage}</>;
}

export default SimpleOidcRouter;
