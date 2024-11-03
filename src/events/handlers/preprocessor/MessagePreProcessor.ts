import type IEvent from "../../IEvent";
import type SnugglesClient from "../../../structure/Client";
import GuildLoggingService from "../../../database/services/GuildLoggingService";
import {type ClientEvents, Events, Message, TextChannel, type User} from "discord.js";
import {client} from "../../../index";
import {LoggingMessageDeletePostProcessor, LoggingMessageUpdatePostProcessor} from "../postprocessor/LoggingPostProcessor";
import {type ILogObj, Logger} from "tslog";

const logger: Logger<ILogObj> = new Logger();

export class MessageCreatePreProcessor implements IEvent {
    public event: keyof ClientEvents = Events.MessageCreate;
    public once = false;

    constructor(private client: SnugglesClient) {}

    async handle(message: Message) {
        const guild = message.guild
        if (!guild) return;

        if (message.author.id == this.client.user?.id) return

        await GuildLoggingService.saveMessage(message);
    }
}

export class MessageDeletePreProcessor implements IEvent {
    public event: keyof ClientEvents = Events.MessageDelete;
    public once = false;

    constructor(private client: SnugglesClient) {}

    async handle(deletedMessage: Message) {
        const guildId = deletedMessage.guildId
        if (!guildId) return

        const loggedMessage = await GuildLoggingService.fetchSavedMessage(deletedMessage.id);
        if (!loggedMessage) return

        const logSettings = await GuildLoggingService.fetchGuildLoggingSettings(guildId);
        const deletedMessageChannelName = await getChannelName(deletedMessage.channelId)
        const author = await getUser(loggedMessage.authorId)

        if (logSettings && logSettings.enabled && logSettings.logDeletes) {
            const loggingChannel = client.channels.cache.get(logSettings.channelId) as TextChannel | undefined
            if (loggingChannel) { // TODO: If cant find logging channel (eg: deleted channel), automatically disable the module?
                await LoggingMessageDeletePostProcessor.handle(
                    deletedMessage, author, loggedMessage, loggingChannel, deletedMessageChannelName
                )
            }
        }
    }
}

export class MessageUpdatePreProcessor implements IEvent {
    public event: keyof ClientEvents = Events.MessageUpdate;
    public once = false;

    constructor(private client: SnugglesClient) {}

    async handle(partialOldMessage: Message, partialNewMessage: Message) {
        const guildId = partialOldMessage.guildId
        if (!guildId) return

        const oldMessage = await GuildLoggingService.fetchSavedMessage(partialOldMessage.id);
        if (!oldMessage) return

        await GuildLoggingService.saveMessage(partialNewMessage)

        const logSettings = await GuildLoggingService.fetchGuildLoggingSettings(guildId);
        const deletedMessageChannelName = await getChannelName(partialNewMessage.channelId)
        const author = await getUser(oldMessage.authorId)

        if (logSettings && logSettings.enabled && logSettings.logEdits) {
            let loggingChannel = this.client.channels.cache.get(logSettings.channelId) as TextChannel | undefined
            if (loggingChannel) { // TODO: If cant find logging channel (eg: deleted channel), automatically disable the module?
                await LoggingMessageUpdatePostProcessor.handle(
                    partialNewMessage, author, oldMessage, loggingChannel, deletedMessageChannelName
                )
            }
        }
    }
}

async function getChannelName(channelId: string): Promise<string> {
    const channel = client.channels.cache.get(channelId) as TextChannel | undefined
        ?? await client.channels.fetch(channelId) as TextChannel | undefined

    if (!channel) return "Unknown Channel" // Um this shouldn't happen I think?
    return channel.name
}

async function getUser(userId: string): Promise<User> {
    return client.users.cache.get(userId)
        ?? await client.users.fetch(userId)
}
