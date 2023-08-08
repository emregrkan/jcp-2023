import UserContext from "@/context/UserContext";
import useSWR from "swr";
import axios from "axios";
import { useContext, useEffect } from "react";
import { useRouter } from "next/router";
import { useForm } from "react-hook-form";

export default function NewJobPosting() {
  const { user: { operator } = { operator: undefined } } =
    useContext(UserContext);

  const router = useRouter();
  const { register, handleSubmit } = useForm();

  const fetcher = (url) =>
    axios
      .get(url, { withCredentials: true })
      .then((res) => res.data)
      .catch((err) => err);

  const { data: departments } = useSWR(
    "http://127.0.0.1:3000/api/departments",
    fetcher
  );

  const handleSave = async (data) => {
    try {
      const response = await axios.post(
        "http://127.0.0.1:3000/api/jobs",
        {
          ...data,
          dueDate: new Date(data.dueDate).toISOString(),
        },
        {
          withCredentials: true,
        }
      );
      router.push(`/jobs/${response.data.id}`);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    departments && !Array.isArray(departments) && router.push("/jobs");
  });

  return (
    operator && (
      <div className="mt-8 bg-base-200 border border-solid rounded-lg p-8 border-base-300">
        <h1 className="font-bold text-4xl text-center">New Job Opening</h1>

        <form
          onSubmit={handleSubmit((data) => handleSave(data))}
          className="mt-8 flex flex-col space-y-4"
        >
          <input
            {...register("title")}
            className="input border border-solid border-base-300"
            placeholder="Job Title"
          />
          <select
            {...register("departmentId")}
            className="select border border-solid border-base-300"
            defaultValue="default"
          >
            {Array.isArray(departments) &&
              departments.map((dep) => (
                <option key={dep.id} value={dep.id}>
                  {dep.name}
                </option>
              ))}
          </select>
          <select
            {...register("workplaceType")}
            className="select border border-solid border-base-300"
          >
            <option value="ON_SITE">On Site</option>
            <option value="HYBRID">Hybrid</option>
            <option value="REMOTE">Remote</option>
          </select>
          <select
            {...register("location")}
            className="select border border-solid border-base-300"
          >
            <option value="Istanbul, Türkiye">Istanbul, Türkiye</option>
            <option value="Ankara, Türkiye">Ankara, Türkiye</option>
            <option value="Izmir, Türkiye">Izmir, Türkiye</option>
          </select>
          <select
            {...register("type")}
            className="select border border-solid border-base-300"
          >
            <option value="FULL_TIME">Full Time</option>
            <option value="PART_TIME">Part Time</option>
            <option value="CONTRACT">Contract</option>
            <option value="TEMPORARY">Temporary</option>
            <option value="VOLUNTEER">Volunteer</option>
            <option value="INTERNSHIP">Internship</option>
            <option value="OTHER">Other</option>
          </select>
          <input {...register("status")} value="ACTIVE" hidden />
          <input
            {...register("dueDate")}
            className="input border border-solid border-base-300"
            type="date"
          />

          <input
            type="submit"
            className="btn btn-accent border border-solid border-base-300"
            value="Save"
          />
        </form>
      </div>
    )
  );
}
