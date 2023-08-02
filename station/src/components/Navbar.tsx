import Link from "next/link";
import Image from "next/image";
import { signIn, useSession } from "next-auth/react";

export default function Navbar() {
  const { status } = useSession();

  return (
    <nav>
      <div>
        <Link href="/">
          <Image src="/logo.svg" alt="Logo of OBSS" width={142} height={58} />
        </Link>
      </div>
      <ul>
        <li>
          <Link href="/">Home</Link>
        </li>
        {status === "authenticated" ? (
          <>
            <li>
              <Link href="/jobs">Jobs</Link>
            </li>
            <li>
              <Link href="/profile">Profile</Link>
            </li>
            <li>
              <Link href="/logout">Log Out</Link>
            </li>
          </>
        ) : (
          <li>
            <a onClick={() => signIn("keycloak")} href="javascript:void(0)">
              Sign In
            </a>
          </li>
        )}
      </ul>
    </nav>
  );
}
