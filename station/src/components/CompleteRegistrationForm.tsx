import UserContext from "@/context/UserContext";
import usePush from "@/hooks/usePush";
import axios, { AxiosError } from "axios";
import { useSession } from "next-auth/react";
import { useContext } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { z } from "zod";

const Inputs = z.object({
  vanityName: z.string(),
});

type Inputs = z.infer<typeof Inputs>;

export default function CompleteRegistrationForm() {
  const { user, setUser } = useContext(UserContext);
  const { data: session } = useSession();
  const push = usePush();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<Inputs>();

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    const inUrl = `https://www.linkedin.com/in/${data.vanityName}`;
    try {
      await axios.put(
        `${process.env.NEXT_PUBLIC_RESOURCE_BASE}/in-user/${user?.id}`,
        {
          inUrl,
        },
        {
          headers: {
            Authorization: `Bearer ${session?.user.accessToken}`,
            "Content-Type": "application/json",
          },
        },
      );
    } catch (error) {
      const err = error as AxiosError;
      if (err.response?.status === 401) {
        push("/signin");
      }
    }
    user && setUser({ ...user, inUrl });
    push("/profile");
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <h1>You must provide your LinkedIn url to continue</h1>
      {errors.vanityName && <span>Your LinkedIn profile link is required</span>}
      <input value="https://www.linkedin.com/in/" disabled />
      <input
        placeholder="example"
        {...register("vanityName", { required: true })}
      />
      <input type="submit" value="Save Changes" />
    </form>
  );
}
