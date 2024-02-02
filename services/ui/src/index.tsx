import React from "react";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import { BrowserRouter } from "react-router-dom";
import { createRoot } from "react-dom/client";

async function enableMsw() {
  if (import.meta.env.MODE.includes("development")) {
    const { worker } = await import("./test-utils/mocks/worker");
    return worker.start({
      serviceWorker: {
        url: "/ui/mockServiceWorker.js",
      },
    });
  }
}

enableMsw().then(() => {
  const container = document.getElementById("root");
  const root = createRoot(container!);
  root.render(
    // <React.StrictMode>
    <BrowserRouter basename={import.meta.env.BASE_URL}>
      <App />
    </BrowserRouter>
    // </React.StrictMode>
  );
});

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
