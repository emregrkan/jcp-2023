import Navbar from "@/components/Navbar";
import UserProvider from "@/provider/UserProvider";
import "@/styles/globals.css";
import { Session } from "next-auth";
import { SessionProvider } from "next-auth/react";
import type { AppType } from "next/app";
import Head from "next/head";
import { Inter } from 'next/font/google'

const inter = Inter({ subsets: ['latin'] })

const App: AppType<{ session: Session | null }> = ({
  Component,
  pageProps: { session, ...pageProps },
}) => {
  return (
    <>
      <SessionProvider session={session}>
        <UserProvider>
          <Head>
            <title>Jobs &amp; Careers | OBSS</title>
          </Head>
          <Navbar />
          <Component {...pageProps} />
        </UserProvider>
      </SessionProvider>
      <style jsx global>{`
        html {
          font-family: ${inter.style.fontFamily};
          font-style: ${inter.style.fontStyle};
          font-weight: ${inter.style.fontWeight};
        }
      `}</style>
    </>
  );
};

export default App;
