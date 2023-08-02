import { PagedJobs } from "@/types/jobs";
import axios from "axios";
import { signIn, useSession } from "next-auth/react";
import useSWR from "swr";

export default function Jobs() {
  const { data: session } = useSession();

  const fetcher = (url: string, token?: string) =>
    axios
      .get(url, { headers: { Authorization: `Bearer ${token}` } })
      .then((res) => res.data);

  const { data, error } = useSWR(
    `${process.env.NEXT_PUBLIC_RESOURCE_BASE}/jobs`,
    (url) => fetcher(url, session?.user.accessToken),
  );

  if (error) {
    console.log(error);

    if (error.response.data.status === 401) {
      signIn("keycloak");
      return;
    }
  }

  const jobs = data as PagedJobs;

  return (
    jobs && (
      <main>
        <div>
          <span>filter section</span>
        </div>
        <div>
          <span>Jobs</span>
          {jobs.content.map((job) => (
            <section key={job.id}>
              <h6>{job.title}</h6>
              <p>
                {(() => {
                  // this is so stupid
                  switch (job.workplaceType) {
                    case "ON_SITE":
                      return "On Site";
                    case "HYBRID":
                      return "Hybrid";
                    case "REMOTE":
                      return "Remote";
                  }
                })()}
              </p>
            </section>
          ))}
        </div>
      </main>
    )
  );
}
