import { Client, Routes } from "discord.js";
import { logger } from "../index";
import Command from "../command/command";

export async function getRestPing(client: Client) {
  if (client.user === null) {
    throw new Error("login first?!?!?1/1/");
  }

  const start = Date.now();
  const data = await client.rest.get(Routes.user("@me"));
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
  commands: Command[],
  guildId?: number | string
) {
  let commandsData = commands.map((command) =>
    command.getParsedCommand().toJSON()
  );

  let responseData;
  if (guildId) {
    responseData = await client.rest.put(
      Routes.applicationGuildCommands(client.user!.id, guildId.toString()),
      { body: commandsData }
    );

    logger.debug(
      `Deployed ${commandsData.length} commands to guild ${guildId}`
    );
  } else {
    responseData = await client.rest.put(
      Routes.applicationCommands(client.user!.id),
      { body: commandsData }
    );

    logger.debug(`Deployed ${commandsData.length} commands globally`);
  }

  logger.debug("API Res:", JSON.stringify(responseData));
}
