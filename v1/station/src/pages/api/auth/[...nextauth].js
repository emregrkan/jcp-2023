import NextAuth from "next-auth";
import { authOptions } from "@/config/auth.config";

export default NextAuth(authOptions);
