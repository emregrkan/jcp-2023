import CompleteRegistrationForm from "@/components/CompleteRegistrationForm";
import { useSession } from "next-auth/react";
import Image from "next/image";
import { useRouter } from "next/router";

export default function Profile() {
  const router = useRouter();
  const { data: session, status } = useSession();
  const { query } = useRouter();

  if (status === "unauthenticated") {
    router.push("/signin");
    return;
  }

  if (query.complete) {
    if (session && session.user.profile && session.user.profile.inUrl) {
      router.push("/profile");
      return;
    }
  } else {
    if (session && session.user.profile && !session.user.profile.inUrl) {
      router.push("/profile?complete=true");
      return;
    }
  }

  return (
    status === "authenticated" &&
    (query.complete ? (
      <CompleteRegistrationForm />
    ) : (
      <div>
        <Image
          alt="Profile Picture"
          src={session.user.profile?.picture ?? "/pfp.svg"}
          width={800}
          height={800}
        />
        <h3>{session.user.name}</h3>
      </div>
    ))
  );
}
