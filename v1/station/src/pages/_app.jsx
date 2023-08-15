import Navbar from "@/components/Navbar";
import UserProvider from "@/provider/UserProvider";
import "@/styles/globals.css";
import { SessionProvider } from "next-auth/react";
import Head from "next/head";
import { Inter } from "next/font/google";

const inter = Inter({ subsets: ["latin"] });

const App = ({ Component, pageProps: { session, ...pageProps } }) => {
  return (
    <>
      <SessionProvider session={session}>
        <UserProvider>
          <Head>
            <title>Jobs &amp; Careers | OBSS</title>
          </Head>
          <Navbar />
          <div className="mt-8 px-8 md:px-64 min-h-full">
            <Component {...pageProps} />
          </div>
        </UserProvider>
      </SessionProvider>
      <style jsx global>{`
        html {
          font-family: ${inter.style.fontFamily};
          font-style: ${inter.style.fontStyle};
          font-weight: ${inter.style.fontWeight};
        }

        #__next {
          min-height: 100%;
        }
      `}</style>
    </>
  );
};

export default App;
