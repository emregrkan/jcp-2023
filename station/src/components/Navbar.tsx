import Link from "next/link";
import Image from "next/image";
import { useSession } from "next-auth/react";

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
        <li>
          <Link href="/jobs">Jobs</Link>
        </li>
        {status !== "loading" &&
          (status === "authenticated" ? (
            <>
              <li>
                <Link href="/profile">Profile</Link>
              </li>
              <li>
                <Link href="/signout">Sign Out</Link>
              </li>
            </>
          ) : (
            <li>
              <Link href="/signin">Sign In</Link>
            </li>
          ))}
      </ul>
    </nav>
  );
}
