import {ActionRowBuilder, ButtonBuilder, ButtonStyle, ChatInputCommandInteraction} from "discord.js";
import {client} from "../../../index.ts";
import {countAllUsers} from "../../../utils/clientUtils.ts";
import {FancyEmbed} from "../../../utils/fancyEmbed.ts";
import {formatCommas, userAsMention} from "../../../utils/stringUtils.ts";
import {findMutualGuilds} from "../../../utils/userUtils.ts";
import CommandBuilder from "../../CommandBuilder.ts";
import type ICommand from "../../ICommand.ts";

export default class SnugglyStats implements ICommand {
    name: string = "snugglystats";
    description: string = "Some fun little global stats about Snuggles";

    getCommand() {
        return new CommandBuilder()
            .setName(this.name)
            .setDescription(this.description)
            .hasSilentToggle(true)
    }

    async handle(interaction: ChatInputCommandInteraction) {
        // Mention the owners if snuggles is sure the command executor can ping them, otherwise use plain text
        const arozePingable = (await findMutualGuilds(interaction.user, client.owners.aroze)).size > 0;
        const lilyPingable = (await findMutualGuilds(interaction.user, client.owners.lily)).size > 0;
        const owners = `${arozePingable ? userAsMention(client.owners.aroze) : `@${client.owners.aroze.tag}`} and ${lilyPingable ? userAsMention(client.owners.lily) : `@${client.owners.lily.tag}`}`;

        const totalServers = formatCommas(interaction.client.guilds.cache.size);
        const totalUsers = formatCommas(await countAllUsers());
        const startTime = `<t:${ Math.floor(interaction.client.readyAt.getTime() / 1000) }:R>`;
        const totalCounts = formatCommas(client.snugglyStats.totalCounts);
        const totalExecutions = formatCommas(client.snugglyStats.totalExecutions)

        const embed = new FancyEmbed("none")
            .setTitle("Snuggles...")
            .setDescription(
                `\n`+
                ` > ...is maintained by ${owners}\n`+
                ` > ...is in **${totalServers}** servers, watching over **${totalUsers}** users\n` +
                ` > ...has been used to execute **${totalExecutions}** commands\n` +
                ` > ...has accepted **${totalCounts}** instances of counting\n` +
                ` > ...was last started ${startTime}\n`
            )

        const actionRow = new ActionRowBuilder().addComponents(
            new ButtonBuilder()
                .setStyle(ButtonStyle.Link)
                .setLabel("I'm open sourced!")
                .setEmoji(":github:1305194858616459375")
                .setURL("https://github.com/UwUAroze/Snuggles"),
            new ButtonBuilder()
                .setStyle(ButtonStyle.Link)
                .setLabel("Invite me :3")
                .setEmoji("<:plussy:1305194878577410153>")
                .setURL("https://discord.com/oauth2/authorize?client_id=1255223867249791007"),
        )

        await interaction.reply({
            fetchReply: true,
            embeds: [embed],
            components: [actionRow],
            ephemeral: interaction.silent
        })
    }
}
