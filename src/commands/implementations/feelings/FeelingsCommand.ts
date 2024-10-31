import type ICommand from "../../ICommand.ts";
import CommandBuilder from "../../CommandBuilder.ts";
import {ChatInputCommandInteraction} from "discord.js";

export default abstract class FeelingsCommand implements ICommand {
    name: string;
    description: string;

    messages = this.getMessages();
    selfMessages = this.getSelfMessages();
    botMessages = this.getBotMessages();

    constructor(name: string, description: string) {
        this.name = name
        this.description = description;
    }

    getCommand() {
        return new CommandBuilder()
            .setName(this.name)
            .setDescription(this.description)
            .hasSilentToggle(true)
            .addUserOption(option => option
                .setName("target")
                .setDescription(`The user to ${this.name}`)
                .setRequired(true)
            );
    }

    async handle(interaction: ChatInputCommandInteraction) {
        const target = interaction.options.getUser("target");
        if (!target) return;

        // Determine the group based on the target user
        const group = target.id === interaction.client.user?.id
            ? this.botMessages
            : target.id === interaction.user.id
                ? this.selfMessages
                : this.messages;

        // Get a random feeling message based on the determined group
        const message = this.randomFeeling(group)
            .replace("{user}", interaction.user.toString())
            .replace("{target}", target.toString());

        await interaction.reply({ content: message, ephemeral: interaction.silent });
    }

    abstract getMessages(): string[];
    abstract getSelfMessages(): string[];
    abstract getBotMessages(): string[];

    randomFeeling(group: string[]): string {
        if (!group || group.length === 0) {
            throw new Error(`No data found for group: ${group} for feeling: ${this.name}`);
        }

        return group[Math.floor(Math.random() * group.length)];
    }

}
