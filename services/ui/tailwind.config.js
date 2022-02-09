module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    fontFamily: {
      sans: ["Rubik", "sans-serif"],
    },
    extend: {
      colors: {
        "cc-primary": {
          100: "#7af8ce",
          200: "#64f7c6",
          300: "#4ef6be",
          400: "#38f5b6",
          500: "#22f4ae",
          600: "#1edb9c",
          700: "#1bc38b",
          800: "#17aa79",
          900: "#149268",
        },
        "cc-secondary": {
          100: "#ff9c9f",
          200: "#ff8b8f",
          300: "#ff7a7e",
          400: "#ff6a6f",
          500: "#ff5a5f",
          600: "#e55155",
          700: "#cc484c",
          800: "#b23e42",
          900: "#993639",
        },
      },
    },
  },
  plugins: [require("@tailwindcss/forms")],
};
