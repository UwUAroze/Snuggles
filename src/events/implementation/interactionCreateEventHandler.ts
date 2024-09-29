import {
  Events,
  type ClientEvents,
  type Interaction,
  type CacheType, type ChatInputCommandInteraction,
} from "discord.js";
import {Logger, type ILogObj} from "tslog";
import type IEvent from "../IEvent.ts";
import SnugglesClient from "../../structure/client.ts";

export const logger: Logger<ILogObj> = new Logger();

export class InteractionCreateEventHandler implements IEvent {
  public event: keyof ClientEvents = Events.InteractionCreate;
  public once: boolean = false;

  constructor(private client: SnugglesClient) {}

  async handle(interaction: Interaction<CacheType>) {
    if (!interaction.isCommand()) return;

    // Get the commands name from the interaction
    const { commandName } = interaction;
    const command = this.client.commands.find(command => command.name === commandName);

    // Check if the commands exists
    if (!command) {
      logger.warn(`Command ${interaction.commandName} not found`);
      return;
    }

    // Set the silent property on the interaction
    let interactionInstance = interaction as ChatInputCommandInteraction;
    interactionInstance.silent = interactionInstance.options.getBoolean("silent", interactionInstance.silent) ?? interactionInstance.silent;

    // Execute the commands using the specified handler
    command.handle(interactionInstance)
      .then(() => logger.debug(`Executed command ${interaction.commandName}`))
      .catch((err: Error) => {
        logger.error(`Error executing command ${interaction.commandName}`, err);
        interaction.reply({ content: "An error occurred while executing this commands", ephemeral: true });
      });
  }
}