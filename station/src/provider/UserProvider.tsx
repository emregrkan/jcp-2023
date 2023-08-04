"use client";

import React, { useEffect, useState } from "react";
import UserContext from "@/context/UserContext";
import { signOut, useSession } from "next-auth/react";
import axios, { AxiosError } from "axios";
import { User } from "@/types/auth";
import usePush from "@/hooks/usePush";
import { useRouter } from "next/router";

export default function UserProvider({
  children,
}: React.PropsWithChildren<{}>) {
  const { update, ...session } = useSession();
  const [user, setUser] = useState<User | null>(null);
  const [refreshed, setRefreshed] = useState(false);
  const [loaded, setLoaded] = useState(false);
  const { route } = useRouter();
  const push = usePush();

  useEffect(() => {
    session.status === "unauthenticated" && route !== "/" && push("/");

    session.status === "authenticated" &&
      !loaded &&
      (async () => {
        if (!refreshed && session.data.user.accessToken) {
          await update({ refresh: true });
          setRefreshed(true);
          return;
        } else if (!refreshed && !session.data.user.accessToken) {
          await signOut();
          return;
        }

        if (refreshed && session.data.user.id && session.data.user.accessToken) {
          let inUrl: string | undefined;

          if (!session.data.user.operator) {
            try {
              const response = await axios.get(
                `${process.env.NEXT_PUBLIC_RESOURCE_BASE}/candidate/${session.data.user.id}`,
                {
                  headers: {
                    Authorization: `Bearer ${session.data.user.accessToken}`,
                  },
                },
              );

              inUrl = response.data.inUrl;
            } catch (err) {
              console.log("UserProvider.tsx: ", err as AxiosError);
            }
          }

          setUser({
            id: session.data.user.id,
            name: session.data.user.name,
            operator: session.data.user.operator,
            picture: session.data.user.picture,
            inUrl,
          });
          setLoaded(true);
        }
      })();
  }, [route, push, loaded, refreshed, update, session]);

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
}
