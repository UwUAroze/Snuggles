import { AttachmentBuilder, EmbedBuilder } from "discord.js";

// YOU'RE WELCOME LILY
const BarStyle = {
  pink: "https://github.com/UwUAroze/Snuggles/blob/v3/assets/img/bar_pink.png?raw=true",
  error:
    "https://github.com/UwUAroze/Snuggles/blob/v3/assets/img/bar_error.png?raw=true",
} as const;

type BarStyle = keyof typeof BarStyle;

export class FancyEmbed extends EmbedBuilder {
  constructor(bar: BarStyle = "pink") {
    super();
    this.setColor(0x2b2d31);
    this.setImage(BarStyle[bar]);
  }
}
