import CompleteRegistrationForm from "@/components/CompleteRegistrationForm";
import UserContext from "@/context/UserContext";
import { useSession } from "next-auth/react";
import Image from "next/image";
import { useContext } from "react";

// todo: add a refresh provider
export default function Profile() {
  const { user } = useContext(UserContext);
  const { data: session } = useSession();

  console.log(user);

  return (
    user &&
    (user.inUrl || user.operator ? (
      <article className="flex flex-col h-full">
        <h1 className="font-extrabold text-3xl">Your Profile</h1>
        <div className="flex-1 mt-6 flex items-center">
          <div className="avatar">
            <div className="w-24 rounded">
              <Image alt="Your profile picture" src={(session && session.user.picture) ?? '/pfp.svg'} width={100} height={100} />
            </div>
          </div>
        </div>
        <article>
          <h2>Name</h2>
          <p>{user.name}</p>
        </article>
        <article>
          <h2>LinkedIn</h2>
          <p>{user.inUrl}</p>
        </article>
      </article>
    ) : (
      <CompleteRegistrationForm />
    ))
  );
}
