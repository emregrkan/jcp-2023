import { signOut } from "next-auth/react";
import { useEffect, useRef } from "react";

// middleman
export default function SignOut() {
  const button = useRef<HTMLButtonElement>(null);
  const stationSignOut = () =>
    signOut({
      callbackUrl: "/",
    });
  // yep another hack
  useEffect(() => button.current?.click());
  return <button ref={button} onClick={stationSignOut} hidden></button>;
}
