import CompleteRegistrationForm from "@/components/CompleteRegistrationForm";
import UserContext from "@/context/UserContext";
import { useSession } from "next-auth/react";
import Image from "next/image";
import { useRouter } from "next/router";
import { useContext } from "react";

export default function Profile() {
  const { data, status } = useSession();
  const { user } = useContext(UserContext);
  const { query, ...router } = useRouter();

  const operator = data?.user.operator;

  if (status === "unauthenticated") {
    router.push("/signin");
    return;
  }

  if (query.complete) {
    if (status === "authenticated" && !!user?.inUrl) {
      router.push("/profile");
      return;
    }
  } else {
    if (status === "authenticated" && !operator && !user?.inUrl) {
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
          src={"/pfp.svg"}
          width={800}
          height={800}
        />
        <h3>{user?.id}</h3>
        <p>{user?.inUrl}</p>
      </div>
    ))
  );
}
