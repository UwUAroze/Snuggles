import {ChatInputCommandInteraction} from "discord.js";
import {evaluate} from "mathjs";
import {FancyEmbed} from "../../../utils/fancyEmbed.ts";
import CommandBuilder from "../../CommandBuilder.ts";
import type ICommand from "../../ICommand.ts";


export default class Evaluate implements ICommand {
    name: string = "evaluate";
    description: string = "Attempts to evaluate a given math expression. Example: (2*5)-7";

    getCommand() {
        return new CommandBuilder()
            .setName(this.name)
            .setDescription(this.description)
            .hasSilentToggle(true)
            .addStringOption(option =>
                option
                    .setName("expression")
                    .setDescription("The math expression to evaluate")
                    .setRequired(true)
            )
    }

    async handle(interaction: ChatInputCommandInteraction) {
        const expression = interaction.options.getString("expression")
        if (!expression) throw new Error("Expression not provided");

        let number;
        try {
            number = evaluate(expression)
        } catch (error) {
            number = NaN
        }

        if (isNaN(number)) {
            const embed = new FancyEmbed("error")
                .setErrorHeader("Your dumbass generated an error.")
                .setDescription("This command is for math, not for whatever degeneracy that was.")
            await interaction.reply({
                embeds: [embed],
                ephemeral: true
            });
            return
        }

        const embed = new FancyEmbed("pink", "vertical")
            .setDescription(`erm.. ${expression} = ${number}`) // todo: randomised response templates here could be fun.

        const response = await interaction.reply({
            embeds: [embed],
            ephemeral: interaction.silent,
            fetchReply: true
        });

        await response.react("<:cat_nerd:1305077348042805249>")
    }
}
