import axios from "axios";
import Image from "next/image";
import Link from "next/link";
import { useRouter } from "next/router";
import useSWR from "swr";

const ProfileCard = ({ application, jobId }) => {
  const { id, applicant, status } = application;
  const router = useRouter();
  const handleStatusChange = async () => {
    try {
      status === "SUBMITTED" &&
        (await axios.post(
          `http://127.0.0.1:3000/api/jobs/${jobId}/applications/${id}/status`,
          {
            status: "VIEWED",
          }
        ));
      router.push(`/profile/${applicant.id}`);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="card flex-row p-4 space-x-4 bg-base-200 border border-solid border-base-300">
      <figure>
        <Image
          src={applicant.profilePicture ?? "/pfp.svg"}
          width={100}
          height={100}
          alt="Applicant profile picture"
        />
      </figure>
      <div>
        <div className="flex items-center space-x-2">
          <h2 className="font-bold text-xl">
            {[applicant.firstName, applicant.lastName].join(" ")}
          </h2>
          <div
            className={`badge bg-base-300 ${
              status === "SUBMITTED" && "bg-success"
            }`}
          >
            {status === "SUBMITTED" ? "NEW" : "VIEWED"}
          </div>
        </div>
        <a href={`mailto:${applicant.email}`}>{applicant.email}</a>
        <div className="mt-4 space-x-2">
          <a
            href={applicant.inUrl}
            className="btn bg-base-100 border border-base-300"
          >
            LinkedIn
          </a>
          <button
            onClick={handleStatusChange}
            className="btn bg-base-100 border border-base-300"
          >
            View Profile
          </button>
        </div>
      </div>
    </div>
  );
};

export default function Applications() {
  const router = useRouter();
  const fetcher = (url) =>
    axios
      .get(url, { withCredentials: true })
      .then((resp) => resp.data)
      .catch((err) => err);

  const { data } = useSWR(
    `http://127.0.0.1:3000/api/jobs/${router.query.id}/applications`,
    fetcher
  );

  return (
    <div className="mt-8">
      <Link
        href={{
          pathname: "/jobs/[id]",
          query: { id: router.query.id },
        }}
      >
        Back to Job Details
      </Link>
      <div className="mt-8">
        <h1 className="text-4xl font-extrabold text-center">Applicants</h1>
        <ul className="mt-8">
          {data &&
            Array.isArray(data) &&
            data.map((application) => (
              <li key={application.id}>
                <ProfileCard
                  application={application}
                  jobId={router.query.id}
                />
              </li>
            ))}
        </ul>
      </div>
    </div>
  );
}
