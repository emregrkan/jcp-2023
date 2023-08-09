import Link from "next/link";
import Image from "next/image";
import { signIn, useSession } from "next-auth/react";
import { useContext } from "react";
import UserContext from "@/context/UserContext";

export default function Navbar() {
  const { user } = useContext(UserContext);
  const { status } = useSession();

  return (
    <div className="navbar border-b border-base-300 md:py-2 px-8 md:px-64">
      <div className="flex-1">
        <Link href="/">
          <Image
            src="/logo-white.svg"
            alt="Logo of OBSS"
            width={142}
            height={58}
          />
        </Link>
      </div>
      <div className="flex-none gap-2">
        {status === "authenticated" ? (
          <>
            <ul className="menu menu-horizontal p-1">
              <li>
                <Link href="/">Home</Link>
              </li>
              <li>
                <Link href="/jobs">Jobs</Link>
              </li>
              {user && user.operator && (
                <li>
                  <Link href="/candidates">Candidates</Link>
                </li>
              )}
            </ul>
            <div className="dropdown dropdown-end">
              <label tabIndex={0} className="btn btn-ghost btn-circle avatar">
                <div className="w-10 rounded-full">
                  <Image
                    alt="Profile Picture"
                    src={(user && user.picture) ?? "/pfp.svg"}
                    width={100}
                    height={100}
                  />
                </div>
              </label>
              <ul
                tabIndex={0}
                className="mt-3 z-[1] p-2 shadow menu menu-sm dropdown-content bg-base-100 rounded-box w-52"
              >
                <li>
                  <Link href="/profile">Profile</Link>
                </li>
                <li>
                  <Link href="/logout">Log Out</Link>
                </li>
              </ul>
            </div>
          </>
        ) : (
          <ul className="menu menu-horizontal px-1">
            <li>
              <a onClick={() => signIn("keycloak")} href="javascript:void(0)">
                Sign In
              </a>
            </li>
          </ul>
        )}
      </div>
    </div>
  );
}
