import { useSession } from "next-auth/react";

export default function Registration() {
  const { data: session, status } = useSession();
  return (
    status !== "loading" &&
    status === "authenticated" && (
      <div>
        <h3>Registration</h3>
      </div>
    )
  );
}
