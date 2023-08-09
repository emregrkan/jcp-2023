import axios from "axios";
import useSWR from "swr";
import Image from "next/image";
import Link from "next/link";
import qs from "querystring";
import { useContext } from "react";
import { useForm } from "react-hook-form";
import UserContext from "@/context/UserContext";

function Candidate({ candidate }) {
  return (
    <div className="card flex-row p-4 space-x-4 bg-base-200 border border-solid border-base-300">
      <figure>
        <Image
          src={candidate.profilePicture ?? "/pfp.svg"}
          width={100}
          height={100}
          alt="Candidate profile picture"
        />
      </figure>
      <div>
        <h2 className="font-bold text-xl">
          {[candidate.firstName, candidate.lastName].join(" ")}
        </h2>
        <a href={`mailto:${candidate.email}`}>{candidate.email}</a>
        <div className="mt-4 space-x-2">
          <a
            href={candidate.inUrl}
            className="btn bg-base-100 border border-base-300"
          >
            LinkedIn
          </a>
          <Link
            href={{
              pathname: "/profile/[id]",
              query: candidate.id,
            }}
            className="btn bg-base-100 border border-base-300"
          >
            View Profile
          </Link>
        </div>
      </div>
    </div>
  );
}

export default function Candidates() {
  const { user } = useContext(UserContext);
  const { register, watch } = useForm();

  const fetcher = (url) =>
    axios
      .get(url, {
        withCredentials: true,
      })
      .then((response) => response.data)
      .catch((err) => err);

  const { data: candidates } = useSWR(
    [
      "http://127.0.0.1:3000/api/candidates/search",
      qs.stringify({ q: watch("q") }),
    ].join("?"),
    fetcher
  );

  return (
    user &&
    user.operator && (
      <div className="mt-8">
        <h1 className="text-4xl font-extrabold text-center">Applicants</h1>
        <form
          className="mt-8 flex justify-center"
          onChange={console.log(watch())}
        >
          <input
            {...register("q")}
            className="input bg-base-200 border border-base-300"
            type="text"
            placeholder="Search Candidates"
          />
        </form>
        <ul className="mt-8">
          {candidates &&
            Array.isArray(candidates) &&
            candidates.map((candidate) => (
              <li key={candidate.id}>
                <Candidate candidate={candidate} />
              </li>
            ))}
        </ul>
      </div>
    )
  );
}
