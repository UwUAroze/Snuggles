import {Collection, type User} from "discord.js";
import {client} from "../index.ts";

export async function findMutualGuilds(user1: User, user2: User) {
    const mutualGuilds = new Collection();

    for (const [guildId, guild] of client.guilds.cache) {
        try {
            const cachedMember1 = guild.members.cache.get(user1.id);
            const cachedMember2 = guild.members.cache.get(user2.id);

            if (cachedMember1 && cachedMember2) {
                mutualGuilds.set(guildId, guild);
                continue;
            }

            if (!cachedMember1) await guild.members.fetch(user1.id);
            if (!cachedMember2) await guild.members.fetch(user2.id);

            mutualGuilds.set(guildId, guild);
        } catch {}
    }
    return mutualGuilds;
}
