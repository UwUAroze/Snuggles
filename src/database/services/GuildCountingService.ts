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
  public static async getCountingDataForGuild(guildId: string): Promise<GuildCounting|null> {
    return prisma.guildCounting.findFirst({
      where: {
        guildId: guildId
      }
    });
  }

  /**
   * Get the counting data for a specific channel
   *
   * @param channelId The channel ID to get the counting data for
   * @returns The counting data for the channel or null if not found
   */
  public static async getCountingDataForChannel(channelId: string): Promise<GuildCounting|null> {
    return prisma.guildCounting.findFirst({
      where: {
        channelId: channelId
      }
    });
  }

  /**
   * Create counting data for a guild
   *
   * @param guildId The ID of the guild in which the counting data is to be created
   * @param channelId The ID of the channel in which the counting data is to be created
   */
  public static async createCountingData(guildId: string, channelId: string): Promise<GuildCounting> {
    return prisma.guildCounting.create({
      data: {
        guildId: guildId,
        channelId: channelId
      }
    });
  }

    /**
     * Update counting data for a guild, automatically correcting the high-score if necessary
     *
     * @param countData The counting data to update
     */
    public static async updateCountData(countData: GuildCounting): Promise<GuildCounting> {
      if (countData.count > countData.highScore) {
        countData.highScore = countData.count;
      }

      return prisma.guildCounting.update({
        where: {
          guildId: countData.guildId,
          channelId: countData.channelId
        },
        data: countData
      });
    }
}
