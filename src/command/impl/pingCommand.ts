import Command from "../command";
import {
  ChatInputCommandInteraction,
  CommandInteraction,
  SlashCommandBuilder,
} from "discord.js";
import { FancyEmbed } from "../../util/fancyEmbed";
import { getRestPing } from "../../util/restUtils";
import { databaseLatency } from "../../database/database";

export default class PingCommand extends Command {
  constructor() {
    super("ping", "Measure's Snuggle's snuggling speed");
  }

  override getCommand() {
    return new SlashCommandBuilder()
      .setName(this.name)
      .setDescription(this.description);
  }

  override async execute(interaction: ChatInputCommandInteraction) {
    const now = Date.now();
    const timeSent = interaction.createdTimestamp;
    const gatewayPing = interaction.client.ws.ping;

    const description = [
      `:satellite: **Discord Latency**`,
      ` - **Gateway Latency** ${gatewayPing}ms`,
      ` - **Response Latency** <a:loading:1255273134769180692>`,
      ` - **Misc Rest Latency** <a:loading:1255273134769180692>`,
      ``,
      `:stopwatch: **Internal Latency**`,
      ` - **Database Latency** <a:loading:1255273134769180692>`,
      ``,
      ` - **Total Command Latency** ${now - timeSent}ms`,
    ];

    const embed = new FancyEmbed()
      .setTitle(`<a:drugged_ping:1255518405440569415>  Pinging...`)
      .setDescription(description.join("\n"));

    const reply = await interaction.reply({
      embeds: [embed],
      fetchReply: true,
      ephemeral: this.getSilent(interaction),
    });

    const responseLatency = reply.createdTimestamp - timeSent;
    description[2] = ` - **Response Latency** ${responseLatency}ms`;
    embed.setDescription(description.join("\n"));
    await this.updateEmbed(interaction, embed, description);

    const restPing = await getRestPing(interaction.client);
    description[3] = ` - **Misc Rest Latency** ${restPing}ms`;
    await this.updateEmbed(interaction, embed, description);

    const dbPing = await databaseLatency();
    description[6] = ` - **Database Latency** ${dbPing}ms`;
    await this.updateEmbed(
      interaction,
      embed,
      description,
      "<:ping:1255273004292771931>  Pong!"
    );
  }

  private async updateEmbed(
    interaction: CommandInteraction,
    embed: FancyEmbed,
    description: string[],
    titleOverride: string | null = null
  ) {
    embed.setDescription(description.join("\n"));
    if (titleOverride) embed.setTitle(titleOverride);
    await interaction.editReply({
      embeds: [embed],
    });
  }
}
