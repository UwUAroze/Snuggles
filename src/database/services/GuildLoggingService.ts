import {type GuildLogging, Prisma} from "@prisma/client";
import type {Message} from "discord.js";
import {type ILogObj, Logger} from "tslog";
import {prisma} from "../../index.ts";

export const logger: Logger<ILogObj> = new Logger();

export default class GuildLoggingService {

    /**
     * Save a message to the database, or update it if it already exists (based on message ID)
     *
     * @param message The message to save
     */
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

        await prisma.loggedMessage.upsert({
            create: {
                messageId: message.id,
                editedAt: message.editedAt,
                guildId: guildId,
                authorId: message.author.id,
                textContent: message.content,
                attachments: {
                    create: attachments
                }
            },
            update: {
                textContent: message.content,
                editedAt: message.editedAt,
                attachments: {
                    deleteMany: {
                        messageId: message.id
                    },
                    create: attachments
                }
            },
            where: {
                messageId: message.id
            },
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
