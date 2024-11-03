"use client";

import { useSession } from "next-auth/react";
import LoginButton from "@/components/auth/LoginButton";

export default function Home() {
  const { data: session, status } = useSession();

  if (status === "loading") {
    return <p>Loading...</p>;
  }

  if (!session) {
    return <div>
      <p>Not logged in</p>;
      <LoginButton />
    </div>
  }

  return (
    <p>Logged in as {session?.user.name}</p>
  );
}
