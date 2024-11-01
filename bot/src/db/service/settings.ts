import type {Guild} from "discord.js";
import {db} from "../index.ts";
import {settings, type Settings} from "../schema.ts";
import {eq} from "drizzle-orm";

export async function getSettings(guild: Guild): Promise<Settings | undefined> {
    const result = await db
        .select()
        .from(settings)
        .where(eq(settings.guild_id, parseInt(guild.id)))
        .limit(1)
        .execute();

    return result[0] as Settings;
}

export async function getSetting(guild: Guild, key: keyof Settings): Promise<string | undefined> {
    const settings = await getSettings(guild);
    return settings?.[key];
}