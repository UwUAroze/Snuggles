import type ICommand from "../../ICommand.ts";
import CommandBuilder from "../../CommandBuilder.ts";
import {ChatInputCommandInteraction, Guild} from "discord.js";
import Randomiser from "../../../utils/randomiser.ts";

export default abstract class FeelingsCommand implements ICommand {
    name: string;
    description: string;

    messages: Map<string, Randomiser<string>> = new Map();
    selfMessages: Map<string, Randomiser<string>> = new Map();
    botMessages: Map<string, Randomiser<string>> = new Map();

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
        let group;
        let defaultList;

        if (target.id === interaction.client.user?.id) {
            group = this.botMessages;
            defaultList = this.getBotMessages();
        } else if (target.id === interaction.user.id) {
            group = this.selfMessages;
            defaultList = this.getSelfMessages();
        } else {
            group = this.messages;
            defaultList = this.getMessages();
        }

        // Get a random feeling message based on the determined group
        const message = this.randomFeeling(interaction.guildId ?? "0", group, defaultList)
            .replace("{user}", interaction.user.toString())
            .replace("{target}", target.toString());

        await interaction.reply({ content: message, ephemeral: interaction.silent });
    }

    abstract getMessages(): string[];
    abstract getSelfMessages(): string[];
    abstract getBotMessages(): string[];

    randomFeeling(guild: string, group: Map<string, Randomiser<string>>, defaultList: string[]): string {
        if (!group.has(guild)) {
            group.set(guild, new Randomiser(defaultList));
        }

        return <string>group.get(guild)?.next();
    }

}
