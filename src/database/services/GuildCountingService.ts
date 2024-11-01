import {Prisma} from "@prisma/client";
import {prisma} from "../../index.ts";
import type { GuildCounting } from '@prisma/client'


export default class GuildCountingService {
  /**
   * Get the counting data for a specific guild
   *
   * @param guildId The guild ID to get the counting data for
   * @returns The counting data for the guild or null if not found
   */
  public async getCountingDataForGuild(guildId: number|string): Promise<GuildCounting|null> {
    return prisma.guildCounting.findFirst({
      where: {
        guildId: Number(guildId)
      }
    });
  }

  /**
   * Get the counting data for a specific channel
   *
   * @param channelId The channel ID to get the counting data for
   * @returns The counting data for the channel or null if not found
   */
  public async getCountingDataForChannel(channelId: string): Promise<GuildCounting|null> {
    return prisma.guildCounting.findFirst({
      where: {
        channelId: Number(channelId)
      }
    });
  }

  /**
   * Create counting data for a guild
   *
   * @param guildId The ID of the guild in which the counting data is to be created
   * @param channelId The ID of the channel in which the counting data is to be created
   */
  public async createCountingData(guildId: string, channelId: string): Promise<GuildCounting> {
    return prisma.guildCounting.create({
      data: {
        guildId: Number(guildId),
        channelId: Number(channelId)
      }
    });
  }
}
