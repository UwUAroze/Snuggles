import {ChatInputCommandInteraction, type SlashCommandOptionsOnlyBuilder} from "discord.js";

/**
 * Represents a commands that can be executed by the bot
 * @interface ICommand
 * @property {string} name - The name of the commands
 * @property {string} description - The description of the commands
 * @property {SlashCommandOptionsOnlyBuilder} getCommand - A function that returns the commands options
 * @property {(interaction: ChatInputCommandInteraction) => Promise<void>} handle - A function that handles the commands
 */
export default interface ICommand {
    name: string;
    description: string;
    getCommand(): SlashCommandOptionsOnlyBuilder;
    handle(interaction: ChatInputCommandInteraction): Promise<void>;
}