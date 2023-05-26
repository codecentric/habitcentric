import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import viteSvgr from "vite-plugin-svgr";

export default defineConfig(() => {
  return {
    test: {
      environment: "jsdom",
      globals: true,
      setupFiles: "src/setupTests.ts",
      coverage: {
        reporter: ["text", "cobertura"],
      },
    },
    server: {
      port: 9004,
      proxy: {
        "/auth": "http://localhost:8080",
      },
    },
    build: {
      outDir: "build",
      assetsDir: ".",
      target: "esnext",
    },
    plugins: [react(), viteSvgr()],
  };
});
