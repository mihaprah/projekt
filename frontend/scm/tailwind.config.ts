import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          light: "#007BFF",
          dark: "#5CA8FF",
        },
        secondary: {
          light: "#FFFFFF",
          dark: "#262626",
        },
        accent: {
          light: "#F8F9FA",
          dark: "#343A40",
        },
        background: {
          light: "#F2F2F2",
          dark: "#4F4F4F",
        },
        danger: "#EA3323"
      },
      width: {
        "150px": "150px",
        "600px": "600px",
      },
      borderRadius: {
        '8': '8px',
      }
    },
  },
  plugins: [require('daisyui')],
};

export default config;