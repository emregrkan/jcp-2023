/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        "gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
        "gradient-conic":
          "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
      },
    },
  },
  plugins: [require("daisyui")],
  daisyui: {
    themes: [
      {
        light: {
          ...require("daisyui/src/theming/themes")["[data-theme=winter]"],
          primary: "#AE1232",
          secondary: "#AE1232",
          accent: "#AE1232",
        },
      },
      {
        black: {
          "color-scheme": "dark",
          primary: "#343232",
          secondary: "#343232",
          accent: "#343232",
          "base-100": "#000000",
          "base-200": "#0D0D0D",
          "base-300": "#1A1919",
          neutral: "#272626",
          "neutral-focus": "#343232",
          info: "#0000ff",
          success: "#008000",
          warning: "#ffff00",
          error: "#ff0000",
        },
      },
    ],
  },
};
