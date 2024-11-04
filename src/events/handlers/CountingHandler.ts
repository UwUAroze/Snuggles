import {
  Events,
  type ClientEvents,
  Message,
  type OmitPartialGroupDMChannel,
  type EmojiIdentifierResolvable, Emoji, GuildEmoji, ReactionEmoji, type User, type Interaction
} from "discord.js";
import {Logger, type ILogObj} from "tslog";
import type IEvent from "../IEvent.ts";
import type SnugglesClient from "../../structure/Client.ts";
import GuildCountingService from "../../database/services/GuildCountingService.ts";
import {column, count, evaluate, number, re} from 'mathjs'
import type {GuildCounting} from "@prisma/client";


export const logger: Logger<ILogObj> = new Logger();

export class CountingHandler implements IEvent {
  public event: keyof ClientEvents = Events.MessageCreate;
  public once = false;

  constructor(private client: SnugglesClient) {}

  async handle(message: OmitPartialGroupDMChannel<Message<boolean>>) {
    const countData = await GuildCountingService.fetchChannelCountingData(message.channelId);
    if (!countData) return;

    // Ignore bot messages and webhooks
    if (message.webhookId != null) return
    if (message.author.bot) return

    let number;

    try {
      number = evaluate(message.content)
    } catch (error) {
      number = NaN
    }

    if (isNaN(number)) {
      // Unable to evaluate - treat as regular chat message
      if (!countData.allowTalkingUsers) await message.delete();
      return;
    }

    this.client.snugglyStats.totalCounts++;

    const lastCounter = countData.lastCounterId
    countData.lastCounterId = message.author.id

    const isNextNumber = number === countData.count + 1
    let currentCount = countData.count

    let failMessage: string | null = null

    if (currentCount != 0 && !countData.allowConsecutiveCounts && lastCounter === message.author.id) {
      failMessage = countData.consecutiveCountingFailMessage
    }

    if (!isNextNumber) {
      failMessage = countData.wrongNumberFailMessage
    }

    if (failMessage) {
      countData.count = 0
      await GuildCountingService.updateCountData(countData)
      await message.reply(CountingHandler.parseCountingMessage(failMessage, message.author, currentCount))
      await message.react(this.getReaction(countData, true))
      return
    }

    countData.count++
    await GuildCountingService.updateCountData(countData)
    await message.react(this.getReaction(countData, false))
  }

  private getReaction(countData: GuildCounting, isFail: boolean): GuildEmoji | string {
    let emoji: GuildEmoji | string | undefined = undefined

    if (countData.count == countData.highScore) {
      emoji = this.client.emojis.cache.get("1302005422043172864")
    }

    if (countData.count % 100 === 0) {
      emoji = "💯"
    }

    if (String(countData.count).includes("69")) {
      emoji = this.client.emojis.cache.get("1302010438367707196")
    }

    if (emoji == null) {
      emoji = this.client.emojis.cache.get("1302005470461952133")
    }

    if (isFail) {
      emoji = this.client.emojis.cache.get("1302005494789181540")
    }

    return emoji ?? "✅" // Last resort/fallback
  }

  static parseCountingMessage(message: string, author: User, count: number): string {
    return message
        .replace("{authorPing}", "<@" + author.id + ">")
        .replace("{count}", count.toString())
  }

}
