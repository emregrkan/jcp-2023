import "@uiw/react-md-editor/markdown-editor.css";
import "@uiw/react-markdown-preview/markdown.css";

import { normalizeScreaming } from "@/util/strings.util";
import axios from "axios";
import moment from "moment";
import dynamic from "next/dynamic";
import Link from "next/link";
import { useRouter } from "next/router";
import useSWR, { mutate } from "swr";
import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";
import UserContext from "@/context/UserContext";

const ActionContext = createContext();

const ActionProvider = ({ children }) => {
  const [edit, setEdit] = useState(false);
  const [details, setDetails] = useState("");

  const handleSave = async (id) => {
    try {
      await axios.post(
        `http://127.0.0.1:3000/api/jobs/${id}/details`,
        {
          details,
        },
        {
          withCredentials: true,
        }
      );
      setEdit(false);
      await mutate(`http://127.0.0.1:3000/api/jobs/${id}/details`);
    } catch (err) {
      console.log(err);
      setEdit(false);
    }
  };

  return (
    <ActionContext.Provider
      value={{ edit, setEdit, details, setDetails, handleSave }}
    >
      {children}
    </ActionContext.Provider>
  );
};

const EditorButton = ({ id }) => {
  const { edit, setEdit, details, handleSave } = useContext(ActionContext);

  if (edit) {
    return (
      <button className="btn" onClick={() => handleSave(id)}>
        Save
      </button>
    );
  }

  return (
    <button className="btn" onClick={() => setEdit(true)}>
      {details.length > 0 ? "Edit" : "Add Details"}
    </button>
  );
};

const OperatorActions = ({ id, status, className }) => {
  const handleOpenClose = async () => {
    try {
      await axios.post(`http://127.0.0.1:3000/api/jobs/${id}/status`, {
        status: status === "CLOSED" ? "ACTIVE" : "CLOSED",
      });
      await mutate(`http://127.0.0.1:3000/api/jobs/${id}`);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className={className}>
      <button onClick={handleOpenClose} className="btn">
        {status === "CLOSED" ? "Open" : "Close"}
      </button>
      <Link
        className="btn"
        href={{
          pathname: "/jobs/[id]/applications",
          query: {
            id,
          },
        }}
      >
        View Applicants
      </Link>
      <EditorButton id={id} />
    </div>
  );
};

const CandidateActions = ({ id, status, applications, className }) => {
  const handleApplication = async () => {
    try {
      status === "ACTIVE" &&
        (await axios.post(
          `http://127.0.0.1:3000/api/jobs/${id}/applications`,
          {},
          {
            withCredentials: true,
          }
        ));
      await mutate(`http://127.0.0.1:3000/api/auth/me/applications`);
    } catch (err) {
      console.log(err);
    }
  };

  const applied =
    applications &&
    Array.isArray(applications) &&
    applications.filter(
      ({ jobApplied: { id: jobId } } = { jobApplied: { id: null } }) =>
        id === jobId
    ).length;

  return (
    <div className={className}>
      {status === "ACTIVE" ? (
        applied ? (
          <button className="btn">Applied</button>
        ) : (
          <button className="btn" onClick={handleApplication}>
            Apply
          </button>
        )
      ) : (
        <button className="btn">Closed For Now</button>
      )}
    </div>
  );
};

const Actions = ({ id, operator, status, applications, className }) => {
  return operator ? (
    <OperatorActions id={id} status={status} className={className} />
  ) : (
    <CandidateActions
      id={id}
      status={status}
      applications={applications}
      className={className}
    />
  );
};

const Markdown = dynamic(
  () => import("@uiw/react-markdown-preview").then((mod) => mod.default),
  { ssr: false }
);

const MDEditor = dynamic(
  () => import("@uiw/react-md-editor").then((mod) => mod.default),
  { ssr: false }
);

const Header = ({ job }) => (
  <div className="space-y-2 text-center">
    <h1 className="text-5xl p-1 font-bold bg-gradient-to-r from-pink-600 to-purple-600 bg-clip-text text-transparent">
      {job.title}, {job.location.split(",")[0]}
    </h1>
    <h2 className="text-xl">
      {job.department} &bull; {normalizeScreaming(job.workplaceType)} &bull;{" "}
      {normalizeScreaming(job.type)}
    </h2>
    <h3>Posted {moment(job.postedAt).fromNow()}</h3>
  </div>
);

const MarkdownView = () => {
  const { edit, details, setDetails } = useContext(ActionContext);

  return edit ? (
    <MDEditor
      value={details}
      onChange={setDetails}
      style={{
        minWidth: "100%",
        marginTop: "2rem",
        marginRight: "2rem",
        backgroundColor: "hsl(var(--b1) / var(--tw-bg-opacity, 1))",
        color: "#c7c7c7",
      }}
    />
  ) : (
    <Markdown
      source={details}
      style={{
        minWidth: "100%",
        marginTop: "2rem",
        marginRight: "2rem",
        backgroundColor: "hsl(var(--b1) / var(--tw-bg-opacity, 1))",
        color: "#c7c7c7",
      }}
    />
  );
};

function JobDetails() {
  const { user: { operator } = { operator: false } } = useContext(UserContext);
  const { edit, setDetails } = useContext(ActionContext);

  const router = useRouter();

  const fetcher = (url) =>
    axios
      .get(url, { withCredentials: true })
      .then((resp) => resp.data)
      .catch((err) => err);

  const { data: applications } = useSWR(
    "http://127.0.0.1:3000/api/auth/me/applications",
    fetcher
  );

  const {
    data: job,
    error: jobError,
    isLoading: jobIsLoading,
  } = useSWR(`http://127.0.0.1:3000/api/jobs/${router.query.id}`, fetcher);

  const { data: details } = useSWR(
    `http://127.0.0.1:3000/api/jobs/${router.query.id}/details`,
    fetcher
  );

  const handleSetDetails = useCallback(
    () => details && !details.response && setDetails(details),
    [details, setDetails]
  );

  useEffect(() => {
    !edit && handleSetDetails();
  }, [edit, handleSetDetails]);

  return !(job && job.id) || jobError || jobIsLoading ? (
    <span>Loading...</span>
  ) : (
    job && (
      <div className="mt-8">
        <Link href="/jobs">Back to Jobs</Link>
        <div className="mt-8">
          <Header job={job} />
          <Actions
            id={router.query.id}
            operator={operator}
            status={job.status}
            applications={applications}
            closed
            className="mt-4 flex justify-center space-x-4"
          />
          <MarkdownView id={router.query.id} operator={operator} />
        </div>
      </div>
    )
  );
}

const JobDetailsWrapper = () => (
  <ActionProvider>
    <JobDetails />
  </ActionProvider>
);

export default JobDetailsWrapper;
