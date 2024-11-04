import {Client, Routes} from "discord.js";
import {logger} from "../index";
import type ICommand from "../commands/ICommand.ts";

export async function getRestPing(client: Client) {
    if (client.user === null) {
        throw new Error("login first?!?!?1/1/");
    }

    const start = Date.now();
    await client.rest.get(
        Routes.user("@me"),
    );
    return Date.now() - start;
}

/**
 * Register the available commands with discord
 * @param client
 * @param commands
 * @param guildId
 */
export async function deployCommands(
  client: Client,
  commands: ICommand[],
  guildId?: number | string
) {
    let commandsData = commands.map(command => command.getCommand().toJSON());

    let responseData
    if (guildId) {
        responseData = await client.rest.put(
          Routes.applicationGuildCommands(client.user!.id, guildId.toString()),
          { body: commandsData }
        );

        logger.debug(`Deployed %s commands to guild %s`, commandsData.length, guildId);
    } else {
        responseData = await client.rest.put(
          Routes.applicationCommands(client.user!.id),
          { body: commandsData }
        );

        logger.debug(`Deployed %s commands globally`, commandsData.length);
    }

    logger.debug("API Res: %s", JSON.stringify(responseData));
}
