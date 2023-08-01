import { GetServerSideProps, InferGetServerSidePropsType } from "next";
import { signOut } from "next-auth/react";
import { useEffect, useRef } from "react";

// middleman
export default function LogOut({
  callbackUrl,
}: InferGetServerSidePropsType<typeof getServerSideProps>) {
  const button = useRef<HTMLButtonElement>(null);
  const stationSignOut = async () => {
    await signOut();
    window.location.href = callbackUrl;
  };
  // yep another hack
  useEffect(() => button.current?.click());
  return <button ref={button} onClick={stationSignOut} hidden></button>;
}

// todo: fix this
export const getServerSideProps: GetServerSideProps<{
  callbackUrl: string;
}> = async () => {
  return {
    props: {
      callbackUrl: `${
        process.env.KEYCLOAK_ISSUER_URL
      }/protocol/openid-connect/logout?post_logout_redirect_url=${encodeURIComponent(
        "http://localhost:3000/",
      )}&client_id=${process.env.KEYCLOAK_CLIENT_ID}`,
    },
  };
};
