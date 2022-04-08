import React from "react";
import Header from "./header/Header";
import OverviewPage from "./overview/OverviewPage";
import Footer from "./footer/Footer";
import SimpleOidcRouter from "./auth/SimpleOidcRouter";
import LandingPage from "./landing/LandingPage";
import { AuthProvider } from "react-oidc-context";
import { oidcConfig } from "./auth/oidcConfig";

function App() {
  return (
    <AuthProvider {...oidcConfig}>
      <div className="divide-y">
        <Header />

        <SimpleOidcRouter
          unauthenticatedPage={<LandingPage />}
          authenticatedPage={<OverviewPage />}
        />

        <Footer />
      </div>
    </AuthProvider>
  );
}

export default App;
