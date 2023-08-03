import axios, { AxiosError } from "axios";
import jwt_decode from "jwt-decode";
import { type GetServerSidePropsContext } from "next";
import {
  getServerSession,
  type DefaultSession,
  type NextAuthOptions,
  Account,
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
    picture: any;
  }

  interface Session extends DefaultSession {
    user: User;
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    operator: boolean;
    accessToken?: string;
    refreshToken?: string;
    expiresAt?: number;
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
        if (session.refresh) {
          token = await refreshToken(token);
        }
      }

      if (account) {
        const roles: string[] = account.access_token
          ? (jwt_decode(account.access_token) as any).realm_access.roles
          : [];

        token.operator = roles.includes("ROLE_OPERATOR");

        if (!token.operator) {
          const url = `${process.env.NEXT_PUBLIC_RESOURCE_BASE}/in-user`;
          const authOptions = {
            headers: {
              Authorization: `Bearer ${account.access_token}`,
            },
          };

          token.picture = await getProfilePicture(account);

          if (token.sub) {
            axios.get(`${url}/${token.sub}`, authOptions).catch((err) => {
              const error = err as AxiosError;
              console.log("src/config/auth.ts: Error: ", error);

              if (error.response?.status === 404) {
                axios
                  .post(
                    url,
                    {
                      fullName: token.name,
                      profilePicture: token.picture,
                      inUrl: null,
                    },
                    authOptions,
                  )
                  .catch((err) =>
                    console.log(
                      "src/config/auth.ts: Error: ",
                      err as AxiosError,
                    ),
                  );
              }
            });
          }
        }

        token.accessToken = account.access_token;
        token.refreshToken = account.refresh_token;
        token.expiresAt = account.expires_at;

        return token;
      }

      return token;
    },
    session: async ({ session, token }) => ({
      ...session,
      user: {
        id: token.sub,
        name: token.name,
        accessToken: token.accessToken,
        operator: token.operator ?? false,
        picture: token.picture,
      },
    }),
  },
  providers: [
    KeycloakProvider({
      clientId: process.env.KEYCLOAK_CLIENT_ID ?? "",
      clientSecret: process.env.KEYCLOAK_CLIENT_SECRET ?? "",
      issuer: process.env.KEYCLOAK_ISSUER_URL,
    }),
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

async function refreshToken(token: JWT): Promise<JWT> {
  const endpoint = `${process.env.KEYCLOAK_ISSUER_URL}/protocol/openid-connect/token`;

  const data = qs.stringify({
    grant_type: "refresh_token",
    client_id: process.env.KEYCLOAK_CLIENT_ID,
    refresh_token: token.refreshToken,
    client_secret: process.env.KEYCLOAK_CLIENT_SECRET,
  });

  const resp = await axios.post(endpoint, data, {
    method: "POST",
    validateStatus: () => true,
  });

  return {
    ...token,
    accessToken: resp.data.access_token,
    refreshToken: resp.data.refresh_token,
    expiresAt: Date.now() + resp.data.expires_in * 1000,
  };
}

async function getProfilePicture(
  account: Account,
): Promise<string | undefined> {
  return axios
    .get(`${process.env.KEYCLOAK_ISSUER_URL}/broker/linkedin/token`, {
      headers: {
        Authorization: `Bearer ${account.access_token}`,
      },
    })
    .then((tk) =>
      axios
        .get(
          "https://api.linkedin.com/v2/me?projection=(id,profilePicture(displayImage~digitalmediaAsset:playableStreams))",
          {
            headers: {
              Authorization: `Bearer ${tk.data.access_token}`,
            },
          },
        )
        .then(
          (pic) =>
            pic.data.profilePicture["displayImage~"].elements
              .pop()
              .identifiers.pop().identifier,
        )
        .catch((err) =>
          console.log("src/config/auth.ts: Error: ", err as AxiosError),
        ),
    )
    .catch((err) =>
      console.log("src/config/auth.ts: Error: ", err as AxiosError),
    );
}
