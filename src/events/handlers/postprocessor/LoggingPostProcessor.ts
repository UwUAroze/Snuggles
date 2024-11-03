import {
    Events,
    type ClientEvents,
    Message,
    type OmitPartialGroupDMChannel,
    TextChannel, AttachmentBuilder, type User, Attachment,
} from "discord.js";
import {Logger, type ILogObj} from "tslog";
import type IEvent from "../../IEvent.ts";
import type SnugglesClient from "../../../structure/Client.ts";
import GuildLoggingService from "../../../database/services/GuildLoggingService.ts";
import {type BarStyle, FancyEmbed} from "../../../utils/fancyEmbed.ts";
import {type GuildLogging, type LoggedMessage, Prisma} from "@prisma/client";
import {client} from "../../../index";

export const logger: Logger<ILogObj> = new Logger();

export class LoggingMessageDeletePostProcessor {
    static async handle(
        deletedMessage: Message,
        author: User,
        loggedMessage: Prisma.LoggedMessageGetPayload<{ include: { attachments: true }}>,
        loggingChannel: TextChannel,
        deletedMessageChannelName: string
    ) {
        let description =
            `\n > Deleted content: ` +
            (loggedMessage.textContent.length === 0
                ? "`None; message had no text content`"
                : `\n${loggedMessage.textContent}\n`) +
            `\n > Perpetrator: <@${loggedMessage.authorId}> \`(${loggedMessage.authorId})\`\n` +
            ` > Channel: ${deletedMessage.channel} \`(${deletedMessage.channelId})\`\n` +
            `\n`;

        if (loggedMessage.attachments.length > 0) {
            description += `\n > Attachments: ${loggedMessage.attachments.length} (uploaded with this message) \n`;
        }

        const eb = new FancyEmbed("error")
            .setAuthor({
                name: `A message by @${author.username} was deleted in #${deletedMessageChannelName}`,
                iconURL: author.displayAvatarURL()
            })
            .setDescription(description)

        await loggingChannel.send({embeds: [eb], files: await collectFiles(loggedMessage.attachments.values())})
    }
}

export class LoggingMessageUpdatePostProcessor {
    static async handle(
        partialNewMessage: Message,
        author: User,
        oldMessage: Prisma.LoggedMessageGetPayload<{ include: { attachments: true }}>,
        loggingChannel: TextChannel,
        updatedMessageChannelName: string
    ) {
        const removedAttachments = oldMessage.attachments.filter(oldAttachment =>
            !partialNewMessage.attachments.some(newAttachment => newAttachment.url === oldAttachment.url)
        )

        let changedText = oldMessage.textContent === partialNewMessage.content ?
            `\n> Text content was not changed`
            : `\n > Message before edit:
            ${!oldMessage.textContent ? '`(None; message had no text content)`' : `${oldMessage.textContent}`}
            \n > Message after edit:
            ${partialNewMessage.content}`

        if (removedAttachments.length > 0) {
            changedText += `\n > Attachments removed: ${removedAttachments.length} (uploaded)`
        }

        const eb = new FancyEmbed()
            .setAuthor({
                name: `A message by @${author.username} was edited in #${updatedMessageChannelName}`,
                iconURL: author.displayAvatarURL()
            })
            .setDescription(
                `${changedText}
                \n > Perpetrator: ${partialNewMessage.author} \`(${partialNewMessage.author.id})\`
                  > Channel: ${partialNewMessage.channel} \`(${partialNewMessage.channel.id})\`
                  > Message: https://discord.com/channels/${partialNewMessage.guildId}/${partialNewMessage.channelId}/${partialNewMessage.id} \`(${partialNewMessage.id})\``
            )

        await loggingChannel.send({embeds: [eb], files: await collectFiles(removedAttachments)})
    }
}

async function collectFiles(attachments: any): Promise<AttachmentBuilder[]> {
    const files: AttachmentBuilder[] = []

    for (const attachment of attachments) {
        try {
            const response = await fetch(attachment.url);
            const buffer = await response.arrayBuffer()

            const discordAttachment = new AttachmentBuilder(Buffer.from(buffer))
                .setName(attachment.name)
                .setDescription(attachment.description)
                .setSpoiler(attachment.spoiler)

            files.push(discordAttachment)
        } catch (error) {
            // todo: something?!
        }
    }

    return files
}

async function getChannelName(client: SnugglesClient, channelId: string): Promise<string> {
    const channel = client.channels.cache.get(channelId) as TextChannel | undefined
        ?? await client.channels.fetch(channelId) as TextChannel | undefined

    if (!channel) return "Unknown Channel" // Um this shouldn't happen I think?
    return channel.name
}

async function getUser(client: SnugglesClient, userId: string): Promise<User> {
    return client.users.cache.get(userId)
        ?? await client.users.fetch(userId)
}
