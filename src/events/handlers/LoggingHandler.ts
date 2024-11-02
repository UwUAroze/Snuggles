import {
    Events,
    type ClientEvents,
    Message,
    type OmitPartialGroupDMChannel,
    TextChannel, AttachmentBuilder,
} from "discord.js";
import {Logger, type ILogObj} from "tslog";
import type IEvent from "../IEvent.ts";
import type SnugglesClient from "../../structure/Client.ts";
import GuildLoggingService from "../../database/services/GuildLoggingService.ts";
import {FancyEmbed} from "../../utils/fancyEmbed.ts";
import {re} from "mathjs";
import {type LoggedMessage, Prisma} from "@prisma/client";


export const logger: Logger<ILogObj> = new Logger();

export class LoggingMessageCreateHandler implements IEvent {
    public event: keyof ClientEvents = Events.MessageCreate;
    public once = false;

    constructor(private client: SnugglesClient) {
    }

    async handle(message: OmitPartialGroupDMChannel<Message<boolean>>) {
        const guild = message.guild
        if (!guild) return;

        await GuildLoggingService.saveMessage(message);
    }

}

export class LoggingMessageDeleteHandler implements IEvent {
    public event: keyof ClientEvents = Events.MessageDelete;
    public once = false;

    constructor(private client: SnugglesClient) {}

    async handle(deletedMessage: OmitPartialGroupDMChannel<Message<boolean>>) {
        logger.debug("deleted message")

        const guildId = deletedMessage.guildId
        if (!guildId) return

        const logSettings = await GuildLoggingService.fetchGuildLoggingSettings(guildId);
        if (!logSettings || !logSettings.enabled || !logSettings.logDeletes) return

        const loggedMessage = await GuildLoggingService.fetchSavedMessage(deletedMessage.id);
        if (!loggedMessage) return

        let deletedMessageChannelName = "Unknown Channel"
        const deletedMessageChannel = this.client.channels.cache.get(deletedMessage.channelId)
        if (deletedMessageChannel) deletedMessageChannelName = (deletedMessageChannel as TextChannel).name

        let description =
            `\n > Deleted content: ` +
            (loggedMessage.textContent.length === 0
                ? "`None; message had no text content`"
                : `\n${loggedMessage.textContent}\n`) +
            `\n > Perpetrator: <@${loggedMessage.authorId}> (\`${loggedMessage.authorId}\`)\n` +
            ` > Channel: ${deletedMessage.channel} (\`${deletedMessage.channelId}\`)\n` +
            `\n`;

        const author = await this.client.users.fetch(loggedMessage.authorId)

        const eb = new FancyEmbed()
            .setAuthor({name: `A message by ${author.username} was deleted in #${deletedMessageChannelName}`})

        if (loggedMessage.attachments.length > 0) {
            description += `\n > Attachments: ${loggedMessage.attachments.length} (uploaded with this message) \n`;
        }

        eb.setDescription(description)

        let loggingChannel = this.client.channels.cache.get(logSettings.channelId)
        if (!loggingChannel) return // TODO: maybe some handling to reset the logging channel if it's deleted, (disable the feature)?

        loggingChannel = loggingChannel as TextChannel
        await loggingChannel.send({embeds: [eb], files: await collectFiles(loggedMessage)})
    }

}

export class LoggingMessageUpdateHandler implements IEvent {
    public event: keyof ClientEvents = Events.MessageUpdate;
    public once = false;

    constructor(private client: SnugglesClient) {
    }

    async handle(message: Message, newMessage: Message) {
        logger.debug(`Message updated ${message.content} -> ${newMessage.content}`)
    }
}

async function collectFiles(loggedMessage: Prisma.LoggedMessageGetPayload<{ include: { attachments: true }}>): Promise<AttachmentBuilder[]> {
    const files: AttachmentBuilder[] = []

    for (const attachment of loggedMessage.attachments.values()) {
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
