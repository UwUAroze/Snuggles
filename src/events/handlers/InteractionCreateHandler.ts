import {
  Events,
  type ClientEvents,
  type Interaction,
  type CacheType, type ChatInputCommandInteraction, ButtonBuilder, ButtonStyle, ActionRowBuilder,
} from "discord.js";
import {Logger, type ILogObj} from "tslog";
import type IEvent from "../IEvent.ts";
import SnugglesClient from "../../structure/Client.ts";
import {client} from "../../index.ts";
import {FancyEmbed} from "../../utils/fancyEmbed.ts";

export const logger: Logger<ILogObj> = new Logger();

export class InteractionCreateHandler implements IEvent {
  public event: keyof ClientEvents = Events.InteractionCreate;
  public once: boolean = false;

  constructor(private client: SnugglesClient) {}

  async handle(interaction: Interaction<CacheType>) {
    if (!interaction.isCommand()) return;
    client.snugglyStats.totalExecutions++;

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
        const embed = new FancyEmbed("error")
            .setErrorHeader("Awh man! Something went wrong!")
            .setDescription("We're not quite sure what caused this error, please let us know if you can reproduce it!!")

        const button = new ButtonBuilder()
            .setStyle(ButtonStyle.Link)
            .setLabel("Support server")
            .setEmoji(":<:marsh:1305069058923692055>:")
            .setURL("https://discord.gg/UTQqmzSQEs") // TODO: Define Discord server link somewhere centrally

        const componentRow = new ActionRowBuilder()
            .addComponents(button);

        // @ts-ignore
        interaction.reply({embeds: [embed], components: [componentRow], ephemeral: true });
      });
  }
}
