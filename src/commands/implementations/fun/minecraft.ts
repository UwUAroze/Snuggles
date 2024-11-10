import {ChatInputCommandInteraction} from "discord.js";
import {FancyEmbed} from "../../../utils/fancyEmbed.ts";
import CommandBuilder from "../../CommandBuilder.ts";
import type ICommand from "../../ICommand.ts";

type NameParts = {
    start: string;
    end: string;
};

export default class Minecraft implements ICommand {
    name: string = "minecraft";
    description: string = "Look up some info on a Minecraft player or server";

    getCommand() {
        return new CommandBuilder()
            .setName(this.name)
            .setDescription(this.description)
            .addSubcommand(subcommand =>
                subcommand
                    .setName("profile")
                    .setDescription("Looks up info on a Minecraft player")
                    .addStringOption(option =>
                        option
                            .setName("player")
                            .setDescription("The username or uuid of the player")
                            .setRequired(true)
                    )
            )
    }

    async handle(interaction: ChatInputCommandInteraction) {
        const isProfile = interaction.options.getSubcommand() === "profile";

        if (isProfile) {
            const player = interaction.options.getString("player");
            if (!player) throw new Error("Player not provided");
            const profile = await fetch(new Request(`https://api.ashcon.app/mojang/v2/user/${player}`, {method: "GET"}))

            const status = profile.status
            if (status !== 200) {
                const embed = new FancyEmbed("error")
                    .setErrorHeader("You messed up!")
                    .setDescription("What in the fuck is `$player`\nThat's not a Minecraft username, nor a UUID, you moron.")
                await interaction.reply({
                    embeds: [embed],
                    ephemeral: true
                });
                return
            }

            const json = await profile.json()
            const uuid = json?.uuid
            const name = json?.username
            const skinLink = json?.textures?.skin?.url

            const capeLink = json?.textures?.cape?.url
            const capeText = capeLink ? `[Download Here](${capeLink})` : "No vanilla cape equipped"

            const createdMilliseconds = Math.round(new Date(json?.created_at).getTime() / 1000)
            const createdDate = createdMilliseconds ? `<t:${createdMilliseconds}:d>, which was <t:${createdMilliseconds}:R>` : "We don't know ;c"

            const embed = new FancyEmbed()
                .setTitle(name)
                .setDescription(
                    ` > UUID: \`${uuid}\`\n` +
                    ` > Creation date: ${createdDate}\n` +
                    `\n` +
                    ` > NameMC: [Click Here](https://namemc.com/profile/${name})\n` +
                    ` > Skin: [Download Here](${skinLink})\n` +
                    ` > Cape: ${capeText}\n\n`
                )
                .setThumbnail(`https://skins.mcstats.com/body/front/${uuid}`)

            await interaction.reply({
                embeds: [embed],
                ephemeral: interaction.silent
            });
        }
    }
}
