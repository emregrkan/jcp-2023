import { type GetServerSidePropsContext } from "next";
import {
  getServerSession,
  type NextAuthOptions,
  type DefaultSession,
} from "next-auth";
import KeycloakProvider from "next-auth/providers/keycloak";
import axios from "axios";
import qs from "querystring";
import { JWT } from "next-auth/jwt";

/**
 * Module augmentation for `next-auth` types. Allows us to add custom properties to the `session`
 * object and keep type safety.
 *
 * @see https://next-auth.js.org/getting-started/typescript#module-augmentation
 */
declare module "next-auth" {
  interface Session extends DefaultSession {
    user: User;
  }

  interface User {
    id: string;
    name: string;
    accessToken?: string;
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    response?: {
      accessToken?: string;
      refreshToken?: string;
      expiresAt?: number;
    };
  }
}

/**
 * Options for NextAuth.js used to configure adapters, providers, callbacks, etc.
 *
 * @see https://next-auth.js.org/configuration/options
 */
export const authOptions: NextAuthOptions = {
  secret: process.env.NEXTAUTH_SECRET,
  callbacks: {
    jwt: async ({ token, account }) => {
      console.log("----   JWT   ----");
      console.log(new Date(Date.now()));
      console.log(new Date(token.response?.expiresAt ?? Date.now()));
      console.log("-----------------");

      if (account) {
        token.response = {
          accessToken: account.access_token,
          refreshToken: account.refresh_token,
          expiresAt: account.expires_at,
        };

        return token;
      }

      if (token.response && Date.now() < (token.response.expiresAt ?? 0)) {
        return token;
      }

      token.response = await refreshToken(token.response);
      return token;
    },
    session: ({ session, token }) => ({
      ...session,
      user: {
        id: token.sub,
        name: token.name,
        accessToken: token.response?.accessToken,
      },
    }),
  },
  providers: [
    KeycloakProvider({
      clientId: process.env.KEYCLOAK_CLIENT_ID ?? "",
      clientSecret: process.env.KEYCLOAK_CLIENT_SECRET ?? "",
      issuer: process.env.KEYCLOAK_ISSUER_URL,
    }),
    /**
     * ...add more providers here.
     *
     * Most other providers require a bit more work than the Discord provider. For example, the
     * GitHub provider requires you to add the `refresh_token_expires_in` field to the Account
     * model. Refer to the NextAuth.js docs for the provider you want to use. Example:
     *
     * @see https://next-auth.js.org/providers/github
     */
  ],
};

/**
 * Wrapper for `getServerSession` so that you don't need to import the `authOptions` in every file.
 *
 * @see https://next-auth.js.org/configuration/nextjs
 */
export const getServerAuthSession = (ctx: {
  req: GetServerSidePropsContext["req"];
  res: GetServerSidePropsContext["res"];
}) => {
  return getServerSession(ctx.req, ctx.res, authOptions);
};

async function refreshToken(
  response: JWT["response"]
): Promise<JWT["response"]> {
  const endpoint = `${process.env.KEYCLOAK_ISSUER_URL}/protocol/openid-connect/token`;

  const data = qs.stringify({
    grant_type: "refresh_token",
    client_id: process.env.KEYCLOAK_CLIENT_ID,
    refresh_token: response?.refreshToken,
    client_secret: process.env.KEYCLOAK_CLIENT_SECRET,
  });

  const resp = await axios.post(endpoint, data, {
    method: "POST",
    validateStatus: () => true,
  });

  console.log("----   Refresh   ----");
  console.log(new Date(Date.now()));
  console.log(new Date(Date.now() + resp.data.expires_in));
  console.log("---------------------");

  return {
    accessToken: resp.data.access_token,
    refreshToken: resp.data.refresh_token,
    expiresAt: Date.now() + resp.data.expires_in * 1000,
  };
}
