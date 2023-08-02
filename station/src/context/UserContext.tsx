import React from "react";
import { User } from "@/types/auth";
import { createContext } from "react";

export default createContext<{
  user: User | null;
  setUser: React.Dispatch<React.SetStateAction<User | null>>;
}>({ user: null, setUser: () => {} });
