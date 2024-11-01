import {
  Events,
  type ClientEvents,
  Message,
  type OmitPartialGroupDMChannel,
  type EmojiIdentifierResolvable, Emoji, GuildEmoji, ReactionEmoji
} from "discord.js";
import {Logger, type ILogObj} from "tslog";
import type IEvent from "../IEvent.ts";
import type SnugglesClient from "../../structure/Client.ts";
import GuildCountingService from "../../database/services/GuildCountingService.ts";
import {count, evaluate, number, re} from 'mathjs'
import type {GuildCounting} from "@prisma/client";


export const logger: Logger<ILogObj> = new Logger();

export class CountingHandler implements IEvent {
  public event: keyof ClientEvents = Events.MessageCreate;
  public once = false;

  constructor(private client: SnugglesClient) {}

  async handle(message: OmitPartialGroupDMChannel<Message<boolean>>) {
    const countData = await GuildCountingService.getCountingDataForChannel(message.channelId);
    if (!countData) return;
    logger.debug(`message: ${message.content} | count: ${countData.count}`)

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

    countData.lastCounterId = message.author.id

    const isCorrect = number === countData.count + 1

    if (!isCorrect) {
      let oldCount = countData.count
      countData.count = 0
      await GuildCountingService.updateCountData(countData)
      await message.reply(
          countData.wrongNumberFailMessage
              .replace("{authorPing}", "<@" + message.author.id + ">")
              .replace("{count}", oldCount.toString())
      )
      await message.react(this.getReaction(countData, isCorrect))
      return
    }

    countData.count++
    await GuildCountingService.updateCountData(countData)
    await message.react(this.getReaction(countData, isCorrect))
  }

  private getReaction(countData: GuildCounting, isCorrect: boolean): GuildEmoji|string {
    let emoji: GuildEmoji|string|undefined = undefined

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

    if (!isCorrect) {
      emoji = this.client.emojis.cache.get("1302005494789181540")
    }

    return emoji ?? "✅" // Last resort/fallback

  }

}
