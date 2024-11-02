import {prisma} from "../../index.ts";
import type {Message} from "discord.js";
import {type ILogObj, Logger} from "tslog";
import {type GuildLogging, type LoggedMessage, Prisma} from "@prisma/client";

export const logger: Logger<ILogObj> = new Logger();

export default class GuildLoggingService {

    public static async saveMessage(message: Message) {
        const guildId = message.guildId
        if (!guildId) return

        const attachments = message.attachments.map(attachment => {
            return {
                url: attachment.url,
                name: attachment.name,
                spoiler: attachment.spoiler,
                description: attachment.description ?? ""
            }
        })

        await prisma.loggedMessage.create({
            data: {
                messageId: message.id,
                guildId: guildId,
                authorId: message.author.id,
                textContent: message.content,
                attachments: {
                    create: attachments
                }
            }
        })
    }

    /**
     * Fetch a saved message by its ID
     *
     * @param messageId The ID of the message to fetch
     * @returns The saved message (at state of last edited) or null if not found
     */
    public static async fetchSavedMessage(messageId: string): Promise<Prisma.LoggedMessageGetPayload<{ include: { attachments: true }}> | null> {
        return prisma.loggedMessage.findFirst({
            where: {
                messageId: messageId
            },
            include: {
                attachments: true
            }
        })!!;
    }

    /**
     * Fetch the logging settings for a guild
     *
     * @param guildId The ID of the guild to fetch the logging settings for
     * @returns The logging settings for the guild or null if not found
     */
    public static async fetchGuildLoggingSettings(guildId: string): Promise<GuildLogging|null> {
        return prisma.guildLogging.findUnique({
            where: {
                guildId: guildId
            }
        })
    }

}
