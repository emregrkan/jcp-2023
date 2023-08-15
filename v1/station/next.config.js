const removeImports = require("next-remove-imports");

/** @type {import('next').NextConfig} */
const nextConfig = removeImports()({
  reactStrictMode: true,
  images: {
    domains: ["media.licdn.com"],
  },
});

module.exports = nextConfig;
