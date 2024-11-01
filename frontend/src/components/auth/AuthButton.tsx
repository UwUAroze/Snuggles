"use client";

import { signIn, signOut, useSession } from "next-auth/react";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {LogOut, User} from "lucide-react";

export default function AuthButton() {
    const { data: session, status } = useSession();

    if (status === "loading") {
        return <p>Loading...</p>;
    }

    if (session) {
        return <p>Logged in as {session.user?.name}</p>;
    }

    return (
        <Button
            onClick={() => signIn("discord")}
        >
            Login with Discord
        </Button>
    );
}