import type ICommand from "../../ICommand.ts";
import CommandBuilder from "../../CommandBuilder.ts";
import {ChatInputCommandInteraction} from "discord.js";

type NameParts = {
    start: string;
    end: string;
};

function splitName(name: string): NameParts {
    const midPoint = Math.ceil(name.length / 2);
    return {
        start: name.slice(0, midPoint),
        end: name.slice(midPoint)
    };
}

function getShipNames(name1: string, name2: string): string[] {
    const first = splitName(name1);
    const second = splitName(name2);

    const combinations = [
        // Basic mashups
        first.start + second.end,
        second.start + first.end,
        first.start + second.start,
        first.end + second.end,

        // Triple combinations
        first.start + second.end + first.end,
        first.start + second.start + second.end,
        second.start + first.start + first.end,
        second.start + first.end + second.end
    ];

    return combinations.map(name => name.toLowerCase());
}

export default class Ship implements ICommand {
    name: string = "ship";
    description: string = "Ships two people together";

    getCommand() {
        return new CommandBuilder()
            .setName(this.name)
            .setDescription(this.description)
            .addSubcommand(subcommand =>
                subcommand
                    .setName("names")
                    .setDescription("Ship using specific names")
                    .addStringOption(option =>
                        option
                            .setName("target")
                            .setDescription("The first name to ship")
                            .setRequired(true)
                    )
                    .addStringOption(option =>
                        option
                            .setName("target2")
                            .setDescription("The second name to ship")
                            .setRequired(false)
                    )
            )
            .addSubcommand(subcommand =>
                subcommand
                    .setName("users")
                    .setDescription("Ship two user profiles")
                    .addUserOption(option =>
                        option
                            .setName("target")
                            .setDescription("The first user to ship")
                            .setRequired(true)
                    )
                    .addUserOption(option =>
                        option
                            .setName("target2")
                            .setDescription("The second user to ship")
                            .setRequired(false)
                    )
            );
    }

    async handle(interaction: ChatInputCommandInteraction) {
        // Get the names of the two users
        const isNames = interaction.options.getSubcommand() === "names";
        const target = isNames
            ? interaction.options.getString("target")
            : interaction.options.getUser("target")?.username;
        const target2 = isNames
            ? interaction.options.getString("target2") ?? interaction.user.username
            : interaction.options.getUser("target2")?.username ?? interaction.user.username;

        if (!target || !target2) return;

        // Get & send the ship name
        const shipNames = getShipNames(target, target2);
        const shipName = shipNames[Math.floor(Math.random() * shipNames.length)];

        await interaction.reply({
            content: `${target} x ${target2} to ${shipName}`,
            ephemeral: interaction.silent
        });
    }
}