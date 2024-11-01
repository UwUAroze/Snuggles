import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {ChevronRight} from "lucide-react";

type Guild = {
    id: string;
    name: string;
    icon: string | null;
    hasBot: boolean;
};

async function fetchGuilds(accessToken: string): Promise<Guild[]> {
    const response = await fetch("https://discord.com/api/users/@me/guilds", {
        headers: {
            Authorization: `Bearer ${accessToken}`,
        },
        cache: "no-store",
    });

    if (!response.ok) {
        throw new Error("Failed to fetch guilds");
    }

    const hasBotGuildIds = [
        "1246242269154246796",
        "1243620020634779769",
    ];

    const guilds = await response.json();
    guilds.forEach((guild: Guild) => {
        guild.hasBot = hasBotGuildIds.includes(guild.id);
    });
    return guilds.sort((a: Guild, b: Guild) => {
        if (hasBotGuildIds.includes(a.id)) {
            return -1;
        }
        if (hasBotGuildIds.includes(b.id)) {
            return 1;
        }
        return 0;
    });
}

export default async function Guilds({ accessToken }: { accessToken: string }) {
    const guilds = await fetchGuilds(accessToken);

    return (
        <div>
            {guilds.map((guild, idx) => (
                <div key={guild.id} className={`flex flex-row justify-between items-center px-4 py-2 my-1 hover:bg-muted transition-colors duration-150 rounded-sm group ${idx === 0 ? "mt-2" : ""} ${idx === guilds.length - 1 ? "mb-2" : ""} ${guild.hasBot ? "cursor-pointer" : "cursor-not-allowed"}`}>
                    <div className="flex flex-row gap-4 items-center">
                        <Avatar className="flex-none">
                            <AvatarImage className={!guild.hasBot ? "opacity-50 group-hover:opacity-100 transition-opacity duration-150" : ""} src={guild.icon ? `https://cdn.discordapp.com/icons/${guild.id}/${guild.icon}.png` : undefined} alt={guild.name} />
                            <AvatarFallback className={!guild.hasBot ? "opacity-50 group-hover:opacity-100 transition-opacity duration-150" : ""}>{guild.name.charAt(0)}</AvatarFallback>
                        </Avatar>
                        <div className="flex-auto">
                            <p className={`${!guild.hasBot ? "opacity-50 group-hover:opacity-100 transition-opacity duration-150" : ""}`}>{guild.name}</p>
                        </div>
                    </div>
                    {guild.hasBot && (
                        <ChevronRight className="h-4 w-4" />
                    )}
                </div>
            ))}
        </div>
    );
}