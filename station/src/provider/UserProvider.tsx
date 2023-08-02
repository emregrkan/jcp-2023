"use client";

import React, { useEffect, useState } from "react";
import UserContext from "@/context/UserContext";
import { useSession } from "next-auth/react";
import axios, { AxiosError } from "axios";
import { InUser } from "@/types/auth";

export default function UserProvider({
  children,
}: React.PropsWithChildren<{}>) {
  const { update, ...session } = useSession();
  const [user, setUser] = useState<InUser | null>(null);
  const [refreshed, setRefreshed] = useState(false);
  const [fetched, setFetched] = useState(false);

  useEffect(() => {
    console.log('useEffect');
    !fetched &&
      session.status === "authenticated" &&
      (async () => {
        if (!refreshed) {
          await update({ refresh: true });
          setRefreshed(true);
        }
        if (refreshed && session.data.user.id) {
          try {
            const response = await axios.get(
              `${process.env.NEXT_PUBLIC_RESOURCE_BASE}/in-user/${session.data.user.id}`,
              {
                headers: {
                  Authorization: `Bearer ${session.data.user.accessToken}`,
                },
              },
            );
            setUser(response.data as InUser);
          } catch (err) {
            console.log("UserProvider.tsx: ", err as AxiosError);
          }
          setFetched(true);
        }
      })();
  }, [fetched, refreshed, update, session]);


  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
}
