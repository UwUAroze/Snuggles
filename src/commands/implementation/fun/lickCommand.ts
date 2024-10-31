import type ICommand from "../../ICommand.ts";
import ExtendedSlashCommandBuilder from "../../ExtendedSlashCommandBuilder.ts";
import {ChatInputCommandInteraction} from "discord.js";
import {randomFeeling} from "../../../utils/feelings.ts";

export default class LickCommand implements ICommand {
    name: string = "lick";
    description: string = "Use this to lick your prey ;)";

    getCommand() {
        return new ExtendedSlashCommandBuilder()
            .setName(this.name)
            .setDescription(this.description)
            .addUserOption(option => option
                .setName("target")
                .setDescription("The user to lick")
                .setRequired(true)
            );
    }

    async handle(interaction: ChatInputCommandInteraction) {
        const target = interaction.options.getUser("target");
        if (!target) return;

        // Determine the group based on the target user
        const group = target.id === interaction.client.user?.id
            ? "bot"
            : target.id === interaction.user.id
                ? "self"
                : "messages";

        // Get a random feeling message based on the determined group
        const message = randomFeeling("LICK", group)
            .replace("{user}", interaction.user.toString())
            .replace("{target}", target.toString());

        await interaction.reply(message);
    }
}