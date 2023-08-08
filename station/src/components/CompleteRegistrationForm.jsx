import UserContext from "@/context/UserContext";
import axios from "axios";
import { useSession } from "next-auth/react";
import { useContext } from "react";
import { useForm } from "react-hook-form";
import qs from "querystring";
import { useRouter } from "next/router";

export default function CompleteRegistrationForm() {
  const router = useRouter();
  const { user, setUser } = useContext(UserContext);
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = async (data) => {
    const inUrl = `https://www.linkedin.com/in/${data.vanityName}`;
    try {
      await axios.post(
        "http://127.0.0.1:3000/api/auth/me/url",
        {
          url: inUrl,
        },
        {
          withCredentials: true,
        }
      );
    } catch (err) {
      console.log(err);
      if (err.response.status === 401) {
        router.push("/");
      }
    }
    user && setUser({ ...user, inUrl });
    router.push("/profile");
  };

  return (
    <form
      className="form-control items-center space-y-4"
      onSubmit={handleSubmit(onSubmit)}
    >
      <h1 className="text-lg">
        You must provide your LinkedIn url to continue
      </h1>
      {errors.vanityName && <span>Your LinkedIn profile link is required</span>}
      <label class="input-group justify-center">
        <span>https://www.linkedin.com/in/</span>
        <input
          {...register("vanityName", { required: true })}
          className="input input-bordered"
          placeholder="example"
        />
      </label>
      <input className="btn" type="submit" value="Save Changes" />
    </form>
  );
}
