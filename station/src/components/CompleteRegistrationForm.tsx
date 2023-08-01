import axios, { AxiosError } from "axios";
import { useSession, signIn } from "next-auth/react";
import { useRouter } from "next/router";
import { SubmitHandler, useForm } from "react-hook-form";
import { z } from "zod";

const Inputs = z.object({
  vanityName: z.string(),
});

type Inputs = z.infer<typeof Inputs>;

export default function CompleteRegistrationForm() {
  const router = useRouter();
  const { data: session, update } = useSession();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<Inputs>();

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    const inUrl = `https://www.linkedin.com/in/${data.vanityName}`;
    try {
      await axios.post(
        `${process.env.NEXT_PUBLIC_RESOURCE_BASE}/in-user`,
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
        router.push("/signin");
      }
    }
    await update({ inUrl });
    router.push("/profile");
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
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
