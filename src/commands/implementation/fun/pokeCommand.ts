import type ICommand from "../../ICommand.ts";
import ExtendedSlashCommandBuilder from "../../ExtendedSlashCommandBuilder.ts";
import {ChatInputCommandInteraction} from "discord.js";
import {randomFeeling} from "../../../utils/feelings.ts";

export default class PokeCommand implements ICommand {
    name: string = "poke";
    description: string = "Pokes your victim";

    getCommand() {
        return new ExtendedSlashCommandBuilder()
            .setName(this.name)
            .setDescription(this.description)
            .addUserOption(option => option
                .setName("target")
                .setDescription("The user to poke")
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
        const message = randomFeeling("POKE", group)
            .replace("{user}", interaction.user.toString())
            .replace("{target}", target.toString());

        await interaction.reply(message);
    }
}