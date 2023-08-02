import React from "react";
import { InUser } from "@/types/auth";
import { createContext } from "react";

export default createContext<{
  user: InUser | null;
  setUser: React.Dispatch<React.SetStateAction<InUser | null>>;
}>({ user: null, setUser: () => {} });
