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

    constructor(private client: SnugglesClient) {
    }

    async handle(message: OmitPartialGroupDMChannel<Message<boolean>>) {
        const guildId = message.guildId
        if (!guildId) return

        const logSettings = await GuildLoggingService.fetchGuildLoggingSettings(guildId);
        if (!logSettings) return
        if (!logSettings.enabled) return

        const loggedMessage = await GuildLoggingService.fetchSavedMessage(message.id);
        if (!loggedMessage) return

        let deletedMessageChannelName = "Unknown Channel"
        const deletedMessageChannel = this.client.channels.cache.get(message.channelId)
        if (deletedMessageChannel) deletedMessageChannelName = (deletedMessageChannel as TextChannel).name

        let description =
            `\n > Deleted content: ` +
            (message.content.length === 0
                ? "`None; message had no text content`"
                : `\n${message.content}\n`) +
            `\n > Perpetrator: ${message.author} (\`${message.author.id}\`)\n` +
            ` > Channel: ${message.channel} (\`${message.channel.id}\`)\n` +
            `\n`;

        const eb = new FancyEmbed()
            .setAuthor({name: `A message by ${message.author.username} was deleted in #${deletedMessageChannelName}`})

        const files: AttachmentBuilder[] = []

        if (message.attachments.size > 0) {
            description += `\n > Attachments: ${message.attachments.size} (uploaded with this message) \n`;
            for (const attachment of message.attachments.values()) {
                try {
                    const response = await fetch(attachment.url);
                    const buffer = await response.arrayBuffer()

                    const discordAttachment = new AttachmentBuilder(Buffer.from(buffer))
                        .setName(attachment.name)

                    files.push(discordAttachment)

                } catch (error) {
                } // todo: something?!
            }
        }

        eb.setDescription(description)

        let loggingChannel = this.client.channels.cache.get(logSettings.channelId)
        if (!loggingChannel) return // TODO: maybe some handling to reset the logging channel if it's deleted, (disable the feature)?

        loggingChannel = loggingChannel as TextChannel
        await loggingChannel.send({embeds: [eb], files: files})

    }

}
