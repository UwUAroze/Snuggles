import {ChatInputCommandInteraction, type SlashCommandOptionsOnlyBuilder} from "discord.js";

/**
 * Represents a command that can be executed by the bot
 * @interface ICommand
 * @property {string} name - The name of the command
 * @property {string} description - The description of the command
 * @property {SlashCommandOptionsOnlyBuilder} getCommand - A function that returns the command options
 * @property {(interaction: ChatInputCommandInteraction) => Promise<void>} handle - A function that handles the command
 */
export default interface ICommand {
    name: string;
    description: string;
    getCommand(): SlashCommandOptionsOnlyBuilder;
    handle(interaction: ChatInputCommandInteraction): Promise<void>;
}