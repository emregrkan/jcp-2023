import moment from "moment";
import Link from "next/link";

export default function JobCard({ job, operator }) {
  const normalize = (text) => {
    const words = text.toLowerCase().split("_");
    for (let i = 0; i < words.length; i++) {
      words[i] = words[i].charAt(0).toUpperCase() + words[i].slice(1);
    }
    return words.join(" ");
  };
  return (
    <div className="card border border-solid border-base-300">
      <div className="card-body">
        <div>
          <h1 className="card-title font-bold hover:underline">
            <Link href={`/jobs/${job.id}`}>{job.title}</Link>
          </h1>
          <h2>
            {job.location} &bull; {normalize(job.type)} &bull;{" "}
            {moment(job.postedAt).fromNow()}
          </h2>
        </div>
        {!operator && (
          <div className="mt-2 card-actions justify-end">
            <Link href={`/jobs/${job.id}`} className="btn btn-active">
              Apply Now
            </Link>
          </div>
        )}
      </div>
    </div>
  );
}
