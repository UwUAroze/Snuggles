import {type SnugglyStats} from "@prisma/client";
import {prisma} from "../../index.ts";


export default class SnugglyStatsService {
  /**
   * Fetches the snuggly stats for the bot from the database. If snuggly stats are NOT found then snuggly stats are created :3
   */
  public static async findOrCreateSnugglyStats(): Promise<SnugglyStats> {
    return await prisma.snugglyStats.findFirst()
        ?? await prisma.snugglyStats.create({});
  }

  /**
   * Upserts the snuggly stats in the database
   *
   * @param snugglyStats The snuggly stats to update
   */
  public static async updateSnugglyStats(snugglyStats: SnugglyStats): Promise<SnugglyStats> {
    return prisma.snugglyStats.upsert({
      where: {
        id: snugglyStats.id
      },
      create: {
        ...snugglyStats
      },
      update: {
        ...snugglyStats
      }
    });
  }

}
