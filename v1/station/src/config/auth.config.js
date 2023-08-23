import axios from "axios";
import jwt_decode from "jwt-decode";
import { getServerSession } from "next-auth";
import KeycloakProvider from "next-auth/providers/keycloak";
import qs from "querystring";

const splitName = (name) => {
  if (name) {
    const split = name.split(" ");

    if (split.length === 1) return { firstName: name, lastName: undefined };

    const lastName = split.pop();
    const firstName = split.join(" ");

    return { firstName, lastName };
  }

  return { firstName: undefined, lastName: undefined };
};

export const authOptions = {
  secret: process.env.NEXTAUTH_SECRET,
  callbacks: {
    jwt: async ({ token, account }) => {
      if (account) {
        const { realm_access, email, exp } = jwt_decode(account.access_token);
        const roles = account.access_token ? realm_access.roles : [];

        token.operator = roles.includes("ROLE_OPERATOR");

        if (!token.operator) {
          token.picture = await getProfilePicture(account);
          const url = `${process.env.RESOURCE_URL}/candidates`;
          const authOptions = {
            headers: {
              Authorization: `Bearer ${account.access_token}`,
            },
          };

          if (token.sub) {
            axios.get(`${url}/me`, authOptions).catch((err) => {
              console.log("src/config/auth.config.js: Error: ", err);

              const { firstName, lastName } = splitName(token.name);

              if (err.response.status === 404) {
                axios
                  .post(
                    url,
                    {
                      firstName,
                      lastName,
                      email: token.email,
                      profilePicture: token.picture,
                    },
                    authOptions
                  )
                  .catch((err) =>
                    console.log("src/config/auth.config.js: Error: ", err)
                  );
              }
            });
          }
        }

        token.email = email;
        token.accessToken = account.access_token;
        token.refreshToken = account.refresh_token;
        token.expiresAt = exp * 1000;
      }

      if (Date.now() > token.expiresAt) {
        token = await refreshAccessToken(token);
      }

      return token;
    },
    session: async ({ session, token }) => {
      const { firstName, lastName } = splitName(token.name);
      return {
        ...session,
        user: {
          id: token.sub,
          firstName,
          lastName,
          email: token.email,
          picture: token.picture,
          operator: token.operator,
          accessToken: token.accessToken,
          refreshToken: token.refreshToken,
        },
      };
    },
  },
  providers: [
    KeycloakProvider({
      clientId: process.env.KEYCLOAK_CLIENT_ID,
      clientSecret: process.env.KEYCLOAK_CLIENT_SECRET,
      issuer: process.env.KEYCLOAK_ISSUER_URL,
    }),
  ],
};

/**
 * Wrapper for `getServerSession` so that you don't need to import the `authOptions` in every file.
 *
 * @see https://next-auth.js.org/configuration/nextjs
 */
export const getServerAuthSession = (req, res) =>
  getServerSession(req, res, authOptions);

async function refreshAccessToken(token) {
  try {
    const endpoint = `${process.env.KEYCLOAK_ISSUER_URL}/protocol/openid-connect/token`;

    const data = qs.stringify({
      grant_type: "refresh_token",
      client_id: process.env.KEYCLOAK_CLIENT_ID,
      refresh_token: token.refreshToken,
      client_secret: process.env.KEYCLOAK_CLIENT_SECRET,
    });

    const response = await axios.post(endpoint, data, {
      method: "POST",
    });

    return {
      ...token,
      accessToken: response.data.access_token,
      refreshToken: response.data.refresh_token,
      expiresAt: Date.now() + response.data.expires_in * 1000,
    };
  } catch (error) {
    console.log("error: RefreshTokenError", error);
    return token;
  }
}

async function getProfilePicture(account) {
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
          }
        )
        .then(
          (pic) =>
            pic.data.profilePicture["displayImage~"].elements
              .pop()
              .identifiers.pop().identifier
        )
        .catch((err) => console.log("src/config/auth.config.js: Error: ", err))
    )
    .catch((err) => console.log("src/config/auth.config.js: Error: ", err));
}
