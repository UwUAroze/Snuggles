import {ChatInputCommandInteraction} from "discord.js";
import {FancyEmbed} from "../../../utils/fancyEmbed.ts";
import CommandBuilder from "../../CommandBuilder.ts";
import type ICommand from "../../ICommand.ts";


export default class UserInfo implements ICommand {
    name: string = "userinfo";
    description: string = "Displays useful information about a user or server member";

    getCommand() {
        return new CommandBuilder()
            .setName(this.name)
            .setDescription(this.description)
            .hasSilentToggle(true)
            .addUserOption(option =>
                option
                    .setName("user")
                    .setDescription("The username or user ID to get information about")
                    .setRequired(true)
            )
    }

    async handle(interaction: ChatInputCommandInteraction) {
        const user = interaction.options.getUser("user")
        if (!user) throw new Error("User not provided");

        const embed = new FancyEmbed()
            .setTitle(`${user.username}`)
            .setThumbnail(user.displayAvatarURL())
            .addFields(
                {
                    name: "Origin",
                    value: `<t:${Math.floor(user.createdTimestamp / 1000)}>`,
                    inline: false
                }
            )

        let finalField = `-# ${user.displayName} (${user.id})`
        if (interaction.guild) {
            const guildId = interaction.guildId
            if (!guildId) throw new Error("Guild ID not found")

            const member = interaction.guild.members.cache.get(user.id)
            if (member) {
                const joined = member.joinedTimestamp
                if (joined) {
                    embed.addFields(
                        {
                            name: "Joined",
                            value: `<t:${Math.floor(joined / 1000)}>`,
                            inline: false
                        }
                    )
                }

                const boosting = member.premiumSinceTimestamp
                if (boosting) {
                    embed.addFields(
                        {
                            name: "Booster",
                            value: `Boosting since <t:${Math.floor(boosting / 1000)}:R>`,
                            inline: false
                        }
                    )
                }

                const roles = member.roles.cache.clone()
                roles.delete(guildId) // The role ID of @everyone is the same as the guild ID, we wanna ignore @everyone 
                if (roles.size > 0) {
                    finalField = roles.map(role => `<@&${role.id}>`).join(" ") + `\n${finalField}`
                }
            }

            embed.addFields(
                {
                    name: "** **",
                    value: finalField,
                    inline: false
                }
            )
        }

        await interaction.reply({
            embeds: [embed],
            ephemeral: interaction.silent,
        });

    }
}
