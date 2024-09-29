import 'discord.js';
import 'discord-api-types/v10';

declare module 'discord-api-types/v10' {
    interface RESTPostAPIChatInputApplicationCommandsJSONBody {
        silentToggle?: boolean;
    }
}

declare module 'discord.js' {
    interface ChatInputCommandInteraction {
        silent: boolean;
    }
}
