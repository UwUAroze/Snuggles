import {EmbedBuilder} from "discord.js";

// YOU'RE WELCOME LILY
const BarStyle = {
    pink: "https://github.com/UwUAroze/Snuggles/blob/v3/assets/img/bar_pink.png?raw=true",
    error: "https://github.com/UwUAroze/Snuggles/blob/v3/assets/img/bar_error.png?raw=true"
} as const;

export type BarStyle = keyof typeof BarStyle;

export class FancyEmbed extends EmbedBuilder {
    constructor(bar: BarStyle = "pink") {
        super();
        this.setColor(0x2b2d31);
        this.setImage(BarStyle[bar]);
    }

    /**
     * Set the author of the embed prefixed with the red forbidden icon, shortcut method for error messages
     *
     * @param title The title of the embed (followed by the forbidden icon)
     */
    setErrorHeader(title: string): FancyEmbed {
        return this.setAuthor({name: title, iconURL: "https://github.com/UwUAroze/Snuggles/blob/v3/assets/img/forbidden.png?raw=true"});
    }
}
