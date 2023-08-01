import axios from "axios";
import { GetServerSidePropsContext } from "next";
import { getServerAuthSession } from "@/config/auth";
import { Exception } from "@/types/exception";
import { type PagedJobs } from "@/types/jobs";

const http = axios.create({
  baseURL: process.env.NEXT_PUBLIC_RESOURCE_BASE,
  validateStatus: () => true,
});

/**
 *
 * @param req
 */
export async function getPagedJobs(
  ctx: GetServerSidePropsContext,
  page?: number,
  size?: number,
): Promise<PagedJobs | Exception> {
  const session = await getServerAuthSession(ctx);
  const response = await http.get("/jobs", {
    params: {
      page,
      size,
    },
    headers: {
      Authorization: `Bearer ${session?.user.accessToken}`,
    },
  });

  return response.data as PagedJobs | Exception;
}
