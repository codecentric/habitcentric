import React from "react";
import Header from "./header/Header";
import OverviewPage from "./overview/OverviewPage";
import Footer from "./footer/Footer";
import LandingPage from "./landing/LandingPage";
import { AuthProvider } from "react-oidc-context";
import { oidc } from "./auth/config/oidc";
import { Route, Routes } from "react-router-dom";
import RequireAuthWhenOidcIsEnabled from "./auth/RequireAuthWhenOidcIsEnabled";

function App() {
  return (
    <AuthProvider {...oidc}>
      <div className="divide-y">
        <Header />

        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route
            path="/overview"
            element={
              <RequireAuthWhenOidcIsEnabled>
                <OverviewPage />
              </RequireAuthWhenOidcIsEnabled>
            }
          />
        </Routes>

        <Footer />
      </div>
    </AuthProvider>
  );
}

export default App;
