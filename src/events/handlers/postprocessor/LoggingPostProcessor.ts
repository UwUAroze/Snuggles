import {Prisma} from "@prisma/client";
import {AttachmentBuilder, Message, TextChannel, type User,} from "discord.js";
import {type ILogObj, Logger} from "tslog";
import {FancyEmbed} from "../../../utils/fancyEmbed.ts";

export const logger: Logger<ILogObj> = new Logger();

export class LoggingMessageDeletePostProcessor {
    static async handle(
        deletedMessage: Message,
        author: User,
        loggedMessage: Prisma.LoggedMessageGetPayload<{ include: { attachments: true }}>,
        loggingChannel: TextChannel,
        channelName: string
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
                name: `A message by @${author.username} was deleted in #${channelName}`,
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
        channelName: string
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
                name: `A message by @${author.username} was edited in #${channelName}`,
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
