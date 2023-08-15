import JobCard from "@/components/JobCard";
import UserContext from "@/context/UserContext";
import axios from "axios";
import Link from "next/link";
import { useContext, useState } from "react";
import useSWR from "swr";

export default function Jobs() {
  const { user: { operator } = { operator: false } } = useContext(UserContext);
  const [pageIndex, setPageIndex] = useState(0);

  const fetcher = (url) =>
    axios
      .get(url, { withCredentials: true })
      .then((res) => res.data)
      .catch((err) => err);

  const { data, error, isLoading } = useSWR(
    `http://127.0.0.1:3000/api/jobs?page=${pageIndex}&jobs=9`,
    fetcher
  );

  const { data: departments } = useSWR(
    "http://127.0.0.1:3000/api/departments",
    fetcher
  );

  const handlePreviousPage = () =>
    pageIndex >= 0 && setPageIndex((page) => page - 1);

  const handleNextPage = () =>
    pageIndex < data.totalPages - 1 && setPageIndex((page) => page + 1);

  return !(data && data.content) || isLoading || error ? (
    <p>Loading...</p>
  ) : (
    <div>
      <span id="importer" className="hidden grid-cols-2 grid-cols-3" hidden />
      <h1 className="font-bold text-4xl text-center">Current Job Openings</h1>
      <form
        className={`mt-8 grid grid-cols-${!!operator + 2} grid-rows-1 gap-4`}
      >
        <input
          type="text"
          placeholder="Search jobs"
          className="input input-bordered bg-base-300 border-transparent placeholder:font-semibold placeholder:text-base-content"
        />
        <select
          className="select select-bordered bg-base-300 border-transparent"
          defaultValue="default"
        >
          <option value="default">Filter by Department</option>
          {Array.isArray(departments) &&
            departments.map((dep) => (
              <option key={dep.id} value={dep.id}>
                {dep.name}
              </option>
            ))}
        </select>
        {operator && (
          <Link
            href="/jobs/new"
            className="btn animate-none bg-base-300 border-transparent"
          >
            New Job Posting
          </Link>
        )}
      </form>
      <div className="mt-8 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {data.content.map((job) => (
          <JobCard key={job.id} job={job} operator={operator} />
        ))}
      </div>
      <div className="mt-8 flex justify-between items-center">
        <button
          onClick={handlePreviousPage}
          className="disabled:font-thin"
          disabled={pageIndex === 0}
        >
          Previous Page
        </button>
        <p>{pageIndex + 1}</p>
        <button
          onClick={handleNextPage}
          className="disabled:font-thin"
          disabled={data.totalPages <= pageIndex + 1}
        >
          Next Page
        </button>
      </div>
    </div>
  );
}
