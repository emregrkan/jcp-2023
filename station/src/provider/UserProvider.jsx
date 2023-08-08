"use client";

import React, { useEffect, useState } from "react";
import UserContext from "@/context/UserContext";
import { signIn, useSession } from "next-auth/react";
import axios from "axios";

export default function UserProvider({ children }) {
  const { status, data } = useSession();
  const [user, setUser] = useState();

  useEffect(() => {
    status === "authenticated" &&
      (async () => {
        if (!data.user.operator) {
          try {
            const response = await axios.get(
              "http://127.0.0.1:3000/api/auth/me",
              {
                withCredentials: true,
              }
            );
            setUser({ ...data.user, ...response.data });
          } catch (err) {
            console.log("error: Could not fetch user details: ", err);
            setUser({ ...data.user });
            await signIn("keycloak");
          }
          return;
        }

        setUser({ ...data.user });
      })();
  }, [status, data]);

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
}
