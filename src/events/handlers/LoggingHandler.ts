import {
    Events,
    type ClientEvents,
    Message,
    type OmitPartialGroupDMChannel,
    TextChannel, AttachmentBuilder, type User,
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

    constructor(private client: SnugglesClient) {}

    async handle(message: OmitPartialGroupDMChannel<Message<boolean>>) {
        const guild = message.guild
        if (!guild) return;

        if (message.author.id == this.client.user?.id) return

        await GuildLoggingService.saveMessage(message);
    }

}

export class LoggingMessageDeleteHandler implements IEvent {
    public event: keyof ClientEvents = Events.MessageDelete;
    public once = false;

    constructor(private client: SnugglesClient) {
    }

    async handle(deletedMessage: OmitPartialGroupDMChannel<Message<boolean>>) {
        const guildId = deletedMessage.guildId
        if (!guildId) return

        const logSettings = await GuildLoggingService.fetchGuildLoggingSettings(guildId);
        if (!logSettings || !logSettings.enabled || !logSettings.logDeletes) return

        let loggingChannel = this.client.channels.cache.get(logSettings.channelId) as TextChannel | undefined
        if (!loggingChannel) return // TODO: maybe some handling to reset the logging channel if it's deleted, (disable the feature)?

        const loggedMessage = await GuildLoggingService.fetchSavedMessage(deletedMessage.id);
        if (!loggedMessage) return

        const deletedMessageChannelName = await getChannelName(this.client, deletedMessage.channelId)

        let description =
            `\n > Deleted content: ` +
            (loggedMessage.textContent.length === 0
                ? "`None; message had no text content`"
                : `\n${loggedMessage.textContent}\n`) +
            `\n > Perpetrator: <@${loggedMessage.authorId}> (\`${loggedMessage.authorId}\`)\n` +
            ` > Channel: ${deletedMessage.channel} (\`${deletedMessage.channelId}\`)\n` +
            `\n`;

        if (loggedMessage.attachments.length > 0) {
            description += `\n > Attachments: ${loggedMessage.attachments.length} (uploaded with this message) \n`;
        }

        const author = await getUser(this.client, loggedMessage.authorId)

        const eb = new FancyEmbed("error")
            .setAuthor({name: `A message by @${author.username} was deleted in #${deletedMessageChannelName}`, iconURL: author.displayAvatarURL()})
            .setDescription(description)

        await loggingChannel.send({embeds: [eb], files: await collectFiles(loggedMessage)})
    }

}

export class LoggingMessageUpdateHandler implements IEvent {
    public event: keyof ClientEvents = Events.MessageUpdate;
    public once = false;

    constructor(private client: SnugglesClient) {
    }

    async handle(partialOldMessage: OmitPartialGroupDMChannel<Message<boolean>>, partialNewMessage: OmitPartialGroupDMChannel<Message<boolean>>) {
        const guildId = partialOldMessage.guildId
        if (!guildId) return

        const logSettings = await GuildLoggingService.fetchGuildLoggingSettings(guildId);
        if (!logSettings) return

        const oldMessage = await GuildLoggingService.fetchSavedMessage(partialOldMessage.id);
        if (!oldMessage) return

        await GuildLoggingService.saveMessage(partialNewMessage)
        if (!logSettings.enabled || !logSettings.logEdits) return

        let loggingChannel = this.client.channels.cache.get(logSettings.channelId) as TextChannel | undefined
        if (!loggingChannel) return // TODO: maybe some handling to reset the logging channel if it's deleted, (disable the feature)?

        const updatedMessageChannelName = await getChannelName(this.client, partialOldMessage.channelId)
        const author = await getUser(this.client, oldMessage.authorId)

        const eb = new FancyEmbed()
            .setAuthor({name: `A message by @${author.username} was edited in #${updatedMessageChannelName}`, iconURL: author.displayAvatarURL()})
            .setDescription(
                `\n > Message before edit:
                ${!oldMessage.textContent ? '`(None; message had no text content)`' : `${oldMessage.textContent}`}
                \n > Message after edit:
                ${partialNewMessage.content}
                \n > Perpetrator: ${partialNewMessage.author} \`(${partialNewMessage.author.id})\`
                  > Channel: ${partialNewMessage.channel} \`(${partialNewMessage.channel.id})\`
                  > Message: https://discord.com/channels/${guildId}/${partialNewMessage.channelId}/${partialNewMessage.id} \`(${partialNewMessage.id})\``
            )

        await loggingChannel.send({embeds: [eb]})
    }
}

async function collectFiles(loggedMessage: Prisma.LoggedMessageGetPayload<{
    include: { attachments: true }
}>): Promise<AttachmentBuilder[]> {
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
