import type { InUser } from "@/types/auth";
import axios from "axios";
import jwt_decode from "jwt-decode";
import { type GetServerSidePropsContext } from "next";
import {
  getServerSession,
  type DefaultSession,
  type NextAuthOptions,
} from "next-auth";
import { JWT } from "next-auth/jwt";
import KeycloakProvider from "next-auth/providers/keycloak";
import qs from "querystring";

/**
 * Module augmentation for `next-auth` types. Allows us to add custom properties to the `session`
 * object and keep type safety.
 *
 * @see https://next-auth.js.org/getting-started/typescript#module-augmentation
 */
declare module "next-auth" {
  interface User {
    id: string;
    name: string;
    accessToken?: string;
    operator: boolean;
    profile?: InUser & {
      picture?: string;
    };
  }

  interface Session extends DefaultSession {
    user: User;
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    user: {
      operator: boolean;
      inUrl?: string;
    };
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
    jwt: async ({ token, account, trigger, session }) => {
      if (trigger === "update") {
        if (session.refresh && Date.now() > (token.response?.expiresAt ?? 0)) {
          token.response = await refreshToken(token.response);
        }

        if (session.inUrl) {
          token.user.inUrl = session.inUrl;
        }
      }

      if (account) {
        const roles: string[] = account.access_token
          ? (jwt_decode(account.access_token) as any).realm_access.roles
          : [];

        token.user = {
          operator: roles.includes("ROLE_OPERATOR"),
        };

        token.response = {
          accessToken: account.access_token,
          refreshToken: account.refresh_token,
          expiresAt: account.expires_at,
        };

        // todo: may be move this to db?
        if (!token.user.operator) {
          const inToken = await axios.get(
            `${process.env.KEYCLOAK_ISSUER_URL}/broker/linkedin/token`,
            {
              headers: {
                Authorization: `Bearer ${token.response.accessToken}`,
              },
            },
          );

          const profilePicture = await axios.get(
            "https://api.linkedin.com/v2/me?projection=(profilePicture(displayImage~digitalmediaAsset:playableStreams))",
            {
              headers: {
                Authorization: `Bearer ${inToken.data.access_token}`,
              },
            },
          );

          try {
            const { data: user } = await axios.get(
              `${process.env.NEXT_PUBLIC_RESOURCE_BASE}/in-user/${token.sub}`,
              {
                headers: { Authorization: `Bearer ${account.access_token}` },
              },
            );

            if (!!user.inUrl) {
              token.user.inUrl = user.inUrl;
            }
          } catch (error) {
            console.log("User does not exists");
          }

          token.picture =
            profilePicture.data.profilePicture[
              "displayImage~"
            ].elements[3].identifiers[0].identifier;
        }

        return token;
      }

      return token;
    },
    session: async ({ session, token }) => ({
      ...session,
      user: {
        id: token.sub,
        name: token.name,
        operator: token.user.operator ?? false,
        profile: {
          inUrl: token.user.inUrl,
          picture: token.picture,
        },
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
     * GitHub  provider requires you to add the `refresh_token_expires_in` field to the Account
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
  response: JWT["response"],
): Promise<JWT["response"]> {
  console.log(response?.refreshToken);
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

  return {
    accessToken: resp.data.access_token,
    refreshToken: resp.data.refresh_token,
    expiresAt: Date.now() + resp.data.expires_in * 1000,
  };
}
