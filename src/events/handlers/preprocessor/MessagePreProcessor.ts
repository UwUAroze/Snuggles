import {type ClientEvents, Events, Message, TextChannel, type User} from "discord.js";
import {type ILogObj, Logger} from "tslog";
import GuildCountingService from "../../../database/services/GuildCountingService";
import GuildLoggingService from "../../../database/services/GuildLoggingService";
import {client} from "../../../index";
import type SnugglesClient from "../../../structure/Client";
import type IEvent from "../../IEvent";
import {
    CountingMessageDeletePostProcessor,
    CountingMessageUpdatePostProcessor
} from "../postprocessor/CountingPostProcessor";
import {
    LoggingMessageDeletePostProcessor,
    LoggingMessageUpdatePostProcessor
} from "../postprocessor/LoggingPostProcessor";

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

        const channel = await getChannel(deletedMessage.channelId)
        const channelName = channel?.name ?? "Unknown Channel"
        const author = await getUser(loggedMessage.authorId)

        const logSettings = await GuildLoggingService.fetchGuildLoggingSettings(guildId);
        if (logSettings && logSettings.enabled && logSettings.logDeletes) {
            const loggingChannel = client.channels.cache.get(logSettings.channelId) as TextChannel | undefined
            if (loggingChannel) { // TODO: If cant find logging channel (eg: deleted channel), automatically disable the module?
                await LoggingMessageDeletePostProcessor.handle(
                    deletedMessage, author, loggedMessage, loggingChannel, channelName
                )
            }
        }

        const countData = await GuildCountingService.fetchGuildLoggingData(guildId)
        if (countData && countData.enabled && countData.warnForDeletes) {
            await CountingMessageDeletePostProcessor.handle(deletedMessage, channel, author, loggedMessage, countData)
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

        const author = await getUser(oldMessage.authorId)

        await GuildLoggingService.saveMessage(partialNewMessage)

        const logSettings = await GuildLoggingService.fetchGuildLoggingSettings(guildId);
        const channel = await getChannel(partialNewMessage.channelId)
        const channelName = channel?.name ?? "Unknown Channel"

        if (logSettings && logSettings.enabled && logSettings.logEdits) {
            let loggingChannel = this.client.channels.cache.get(logSettings.channelId) as TextChannel | undefined
            if (loggingChannel) { // TODO: If cant find logging channel (eg: deleted channel), automatically disable the module?
                await LoggingMessageUpdatePostProcessor.handle(
                    partialNewMessage, author, oldMessage, loggingChannel, channelName
                )
            }
        }

        const countData = await GuildCountingService.fetchGuildLoggingData(guildId)
        if (countData && countData.enabled && countData.warnForEdits) {
            await CountingMessageUpdatePostProcessor.handle(channel, author, oldMessage, countData)
        }
    }
}

async function getChannel(channelId: string): Promise<TextChannel|undefined> {
    return client.channels.cache.get(channelId) as TextChannel | undefined
        ?? await client.channels.fetch(channelId) as TextChannel | undefined
}

async function getUser(userId: string): Promise<User> {
    return client.users.cache.get(userId)
        ?? await client.users.fetch(userId)
}
