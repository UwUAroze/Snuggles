import {integer, pgTable, text} from "drizzle-orm/pg-core";

export const settings = pgTable("settings", {
    guild_id: integer("guild_id").notNull(),
    is_minehut: text("is_minehut").notNull(),
});

export interface Settings {
    is_minehut: string;
}