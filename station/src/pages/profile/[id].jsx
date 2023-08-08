import axios from "axios";
import Image from "next/image";
import { useRouter } from "next/router";
import useSWR from "swr";
// todo: add a refresh provider

export default function Profile() {
  const router = useRouter();

  const fetcher = (url) =>
    axios
      .get(url, {
        withCredentials: true,
      })
      .then((response) => response.data)
      .catch((err) => err);

  const { data: profile, isLoading } = useSWR(
    ["http://127.0.0.1:3000/api/candidates", router.query.id].join("/"),
    fetcher
  );

  return !profile || isLoading ? (
    <p>Loading...</p>
  ) : (
    <div className="space-y-6">
      <div className="flex items-center space-x-4">
        <Image
          src={profile.profilePicture ?? "/pfp.svg"}
          alt="Profile picture"
          width={100}
          height={100}
          className="h-16 w-16 rounded-lg"
        />
        <div>
          <h2 className="text-2xl font-semibold">
            {profile.firstName} {profile.lastName}
          </h2>
          {profile.operator && (
            <p className="text-gray-500">Human Resources at OBSS</p>
          )}
          {profile && profile.headline && (
            <p className="text-gray-500">{profile.headline}</p>
          )}
        </div>
      </div>

      {profile && profile.about && (
        <div>
          <h3 className="text-xl font-semibold">About</h3>
          <p>{profile.about}</p>
        </div>
      )}

      {profile && profile.experience && (
        <div>
          <h3 className="text-xl font-semibold">Experience</h3>
          {profile.experience.map((exp, index) => (
            <div key={index} className="space-y-2">
              <h4 className="text-lg font-semibold">{exp.title}</h4>
              <p>{exp.companyName}</p>
              <p>{exp.duration}</p>
            </div>
          ))}
        </div>
      )}

      {profile && profile.education && (
        <div>
          <h3 className="text-xl font-semibold">Education</h3>
          {profile.education.map((edu, index) => (
            <div key={index} className="space-y-2">
              <h4 className="text-lg font-semibold">{edu.school}</h4>
              {edu.field && <p>{edu.field}</p>}
              <p>{edu.degree}</p>
              <p>{edu.duration}</p>
            </div>
          ))}
        </div>
      )}

      {(profile.email || (profile && profile.location) || profile.inUrl) && (
        <div>
          <h3 className="text-xl font-semibold">Contact</h3>
          {profile.email && <p>Email: {profile.email}</p>}
          {profile && profile.location && <p>Location: {profile.location}</p>}
          {profile.inUrl && (
            <p>
              LinkedIn:{" "}
              <a href={profile.inUrl} className="text-blue-500 hover:underline">
                {profile.inUrl}
              </a>
            </p>
          )}
        </div>
      )}
    </div>
  );
}
