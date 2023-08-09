import useSWR from "swr";

export default function Applications() {
  const fetcher = (url) =>
    axios
      .get(url, {
        withCredentials: true,
      })
      .then((response) => response.data)
      .catch((err) => err);

  const { data: applications } = useSWR(
    "http://127.0.0.1:3000/api/auth/me/applications",
    fetcher
  );

  console.log(applications);

  return (
    <div>
      <ul>
        {applications &&
          Array.isArray(applications) &&
          applications.map((application) => (
            <li key={application.id}>{application.jobApplied.title}</li>
          ))}
      </ul>
    </div>
  );
}
