import CompleteRegistrationForm from "@/components/CompleteRegistrationForm";
import UserContext from "@/context/UserContext";
import axios from "axios";
import Image from "next/image";
import { useContext, useEffect, useState } from "react";
import useSWR from "swr";
// todo: add a refresh provider

export default function Profile() {
  const { user } = useContext(UserContext);
  const fetcher = (url) =>
    user &&
    !user.operator &&
    axios
      .get(url, {
        withCredentials: true,
      })
      .then((response) => response.data)
      .catch((err) => err);
  const { data, isLoading } = useSWR(
    "http://127.0.0.1:3000/api/auth/me/profile",
    fetcher
  );

  return (
    user &&
    (user.inUrl || user.operator ? (
      isLoading ? (
        <p>Loading...</p>
      ) : (
        <div className="space-y-6">
          <div className="flex items-center space-x-4">
            <Image
              src={user.picture ?? "/pfp.svg"}
              alt="Profile picture"
              width={100}
              height={100}
              className="h-16 w-16 rounded-lg"
            />
            <div>
              <h2 className="text-2xl font-semibold">
                {user.firstName} {user.lastName}
              </h2>
              {user.operator && (
                <p className="text-gray-500">Human Resources at OBSS</p>
              )}
              {data && data.headline && (
                <p className="text-gray-500">{data.headline}</p>
              )}
            </div>
          </div>

          {data && data.about && (
            <div>
              <h3 className="text-xl font-semibold">About</h3>
              <p>{data.about}</p>
            </div>
          )}

          {data && data.experience && (
            <div>
              <h3 className="text-xl font-semibold">Experience</h3>
              {data.experience.map((exp, index) => (
                <div key={index} className="space-y-2">
                  <h4 className="text-lg font-semibold">{exp.title}</h4>
                  <p>{exp.companyName}</p>
                  <p>{exp.duration}</p>
                </div>
              ))}
            </div>
          )}

          {data && data.education && (
            <div>
              <h3 className="text-xl font-semibold">Education</h3>
              {data.education.map((edu, index) => (
                <div key={index} className="space-y-2">
                  <h4 className="text-lg font-semibold">{edu.school}</h4>
                  {edu.field && <p>{edu.field}</p>}
                  <p>{edu.degree}</p>
                  <p>{edu.duration}</p>
                </div>
              ))}
            </div>
          )}

          {(user.email || (data && data.location) || user.inUrl) && (
            <div>
              <h3 className="text-xl font-semibold">Contact</h3>
              {user.email && <p>Email: {user.email}</p>}
              {data && data.location && <p>Location: {data.location}</p>}
              {user.inUrl && (
                <p>
                  LinkedIn:{" "}
                  <a
                    href={user.inUrl}
                    className="text-blue-500 hover:underline"
                  >
                    {user.inUrl}
                  </a>
                </p>
              )}
            </div>
          )}
        </div>
      )
    ) : (
      <CompleteRegistrationForm />
    ))
  );
}
