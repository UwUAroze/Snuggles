import {type GuildCounting, Prisma} from "@prisma/client";
import {Message, TextChannel, type User,} from "discord.js";
import {evaluate} from "mathjs";
import {type ILogObj, Logger} from "tslog";
import {CountingHandler} from "../CountingHandler";

export const logger: Logger<ILogObj> = new Logger();

export class CountingMessageDeletePostProcessor {
    static async handle(
        deletedMessage: Message,
        channel: TextChannel|undefined,
        author: User,
        loggedMessage: Prisma.LoggedMessageGetPayload<{ include: { attachments: true } }>,
        countData: GuildCounting,
    ) {
        if (loggedMessage.authorId != countData.lastCounterId) return
        if (evaluate(loggedMessage.textContent) !== countData.count) return
        await channel?.send(CountingHandler.parseCountingMessage(countData.caughtDeleteMessage, author, countData.count))
    }
}

export class CountingMessageUpdatePostProcessor {
    static async handle(
        channel: TextChannel|undefined,
        author: User,
        oldMessage: Prisma.LoggedMessageGetPayload<{ include: { attachments: true } }>,
        countData: GuildCounting,
    ) {
        if (oldMessage.authorId != countData.lastCounterId) return
        if (evaluate(oldMessage.textContent) !== countData.count) return
        if (oldMessage.editedAt != null) return
        await channel?.send(CountingHandler.parseCountingMessage(countData.caughtEditMessage, author, countData.count))
    }
}
