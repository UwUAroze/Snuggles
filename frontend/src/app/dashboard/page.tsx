import { getServerSession } from "next-auth/next";
import { authOptions } from "@/app/api/auth/[...nextauth]/route";
import { redirect } from "next/navigation";
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";
import Guilds from "@/components/Guilds";
import GuildsLoading from "@/components/GuildsLoading";
import React, { Suspense } from "react";
import {Button} from "@/components/ui/button";

export default async function DashboardPage() {
    const session = await getServerSession(authOptions);

    if (!session) {
        redirect("/auth/login");
    }

    return (
        <div className="min-h-screen flex items-center justify-center">
            <Card className="w-[400px]">
                <CardHeader>
                    <CardTitle>Snuggles Dashboard</CardTitle>
                    <CardDescription>Select a server to configure.</CardDescription>
                </CardHeader>
                <CardContent>
                    <ScrollArea className="h-[350px]">
                        <Suspense fallback={<GuildsLoading/>}>
                            <Guilds accessToken={session!.accessToken as string}/>
                        </Suspense>
                        <div className="absolute top-0 h-2 w-full bg-gradient-to-t from-transparent to-background"></div>
                        <div className="absolute bottom-0 h-2 w-full bg-gradient-to-b from-transparent to-background"></div>
                    </ScrollArea>
                </CardContent>
                <CardFooter className="justify-between">
                    <Button variant="outline">
                        Logout
                    </Button>
                    <Button variant="link">
                        Invite Snuggles
                    </Button>
                </CardFooter>
            </Card>
        </div>
    );
}