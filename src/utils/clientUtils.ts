import {client} from "../index.ts";

export async function countAllUsers(): Promise<number> {
    let total = 0;

    for (const guild of client.guilds.cache.values()) {
        total += guild.memberCount;
    }

    return total;
}
