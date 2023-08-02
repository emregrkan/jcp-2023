import CompleteRegistrationForm from "@/components/CompleteRegistrationForm";
import UserContext from "@/context/UserContext";
import { useContext } from "react";

export default function Profile() {
  const { user } = useContext(UserContext);

  return (
    user &&
    (user.inUrl || user.operator ? (
      <div>
        <p>{user.name}</p>
        <p>{user.inUrl}</p>
      </div>
    ) : (
      <CompleteRegistrationForm />
    ))
  );
}
