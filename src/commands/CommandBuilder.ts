import {SlashCommandBuilder} from '@discordjs/builders';

export default class CommandBuilder extends SlashCommandBuilder {
    private silentToggle: boolean = false;

    /**
     * Enables or disables the silent toggle option.
     * @param {boolean} value - Indicates whether the silent toggle should be enabled.
     * @returns {this}
     */
    hasSilentToggle(value: boolean): this {
        this.silentToggle = value;
        return this;
    }

    override toJSON() {
        if (this.silentToggle) {
            super.addBooleanOption(option => option
                .setName("silent")
                .setDescription(`Whether the command should only show its response to you. Default for this command: ${this.silentToggle}`)
                .setRequired(false)
            );
        }

        return super.toJSON();
    }
}