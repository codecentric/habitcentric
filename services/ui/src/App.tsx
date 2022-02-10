import React from "react";
import Header from "./Header";
import OverviewPage from "./OverviewPage";
import Footer from "./Footer";

function App() {
  return (
    <div className="divide-y">
      <Header />

      <OverviewPage />

      <Footer />
    </div>
  );
}

export default App;
