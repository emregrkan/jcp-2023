import Navbar from "@/components/Navbar";
import "@/styles/globals.css";
import { Session } from "next-auth";
import { SessionProvider } from "next-auth/react";
import type { AppType } from "next/app";
import Head from "next/head";

const App: AppType<{ session: Session | null }> = ({
  Component,
  pageProps: { session, ...pageProps },
}) => {
  return (
    <SessionProvider session={session}>
      <Head>
        <title>Jobs &amp; Carees | OBSS</title>
      </Head>
      <Navbar />
      <Component {...pageProps} />
    </SessionProvider>
  );
};

export default App;
