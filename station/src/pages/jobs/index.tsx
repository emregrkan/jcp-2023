import { getPagedJobs } from "@/api/jobs";
import moment from "moment";
import { getServerAuthSession } from "@/config/auth";
import type { Exception } from "@/types/exception";
import type { PagedJobs } from "@/types/jobs";
import {
  GetServerSideProps,
  GetServerSidePropsContext,
  InferGetServerSidePropsType,
} from "next";

export default function Jobs({
  data,
}: InferGetServerSidePropsType<typeof getServerSideProps>) {
  return (
    <main>
      <div>
        <span>filter section</span>
      </div>
      <div>
        <span>Jobs</span>
        {data?.content.map((job) => (
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
  );
}

export const getServerSideProps: GetServerSideProps<{
  data: PagedJobs | null;
}> = async (ctx: GetServerSidePropsContext) => {
  const response = await getPagedJobs(ctx);
  if ((response as Exception).status === 401) {
    return {
      redirect: {
        permanent: true,
        destination: "/signin",
      },
      props: {
        data: null,
      },
    };
  }

  return {
    props: {
      data: response as PagedJobs,
    },
  };
};
