import { signIn } from "next-auth/react";
import { useEffect, useRef } from "react";

// middleman
export default function SignIn() {
  const button = useRef<HTMLButtonElement>(null);
  const stationSignIn = () => signIn("linkedin", { callbackUrl: "/" });
  // evil sign in redirect hack
  useEffect(() => button.current?.click());
  return <button ref={button} onClick={stationSignIn} hidden></button>;
}
