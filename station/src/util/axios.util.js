import { getServerAuthSession } from "@/config/auth.config";
import axios from "axios";
import { signIn } from "next-auth/react";

export default function http(req, res) {
  const http = axios.create({
    baseURL: process.env.RESOURCE_URL,
  });

  http.interceptors.request.use(
    async (config) => {
      const session = await getServerAuthSession(req, res);
      config.headers = {
        Authorization: `Bearer ${session.user.accessToken}`,
      };
      return config;
    },
    (err) => console.log("AxiosRequestError: ", err)
  );

  http.interceptors.response.use(
    (response) => response,
    async (err) => {
      console.log("AxiosResponseError: ", err);
      await signIn("keycloak");
    }
  );

  return http;
}
