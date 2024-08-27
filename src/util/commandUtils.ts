import Command from "../command/command";
import { logger } from "../index";
import {
  ChatInputCommandInteraction,
  Client,
  Events,
  Routes,
} from "discord.js";

/**
 * Handle command execution by hooking into the interactionCreate listener
 * @param client
 * @param commands
 */
export async function handleCommands(client: Client, commands: Command[]) {
  client.on(Events.InteractionCreate, async (interaction) => {
    if (!interaction.isCommand()) return;

    const { commandName } = interaction;

    const command = commands.find((command) => command.name === commandName);
    if (!command) return;

    command
      .execute(interaction as ChatInputCommandInteraction)
      .then(() => logger.debug(`Command ${commandName} executed`))
      .catch((err) => {
        const referenceId = Math.floor(Math.random() * 0xffffffff)
          .toString(16)
          .padStart(8, "0");

        logger.error(
          `Error executing command ${commandName}. Ref ID: ${referenceId}`,
          err
        );
        interaction.reply({
          content: `There was an error while executing the command \n> Ref ID: ${referenceId}`,
          ephemeral: true,
        });
      });
  });
}
