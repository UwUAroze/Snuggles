import {GatewayIntentBits, Partials} from "discord.js";
import {Logger, type ILogObj} from "tslog";
import config from "./config/config";
// import {databaseConnect} from "./database/database";
// import UserMessageService from "./database/services/userMessageService";
import SnugglesClient from "./structure/Client.ts";
import {PrismaClient} from "@prisma/client";

export const prisma = new PrismaClient()

export const logger: Logger<ILogObj> = new Logger();
// export const userMessageService = new UserMessageService();

export let client: SnugglesClient;

(async () => {
    // const now = new Date();
    // await databaseConnect(config().database.mongo_uri);
    // logger.info(`Connected to database in ${new Date().getTime() - now.getTime()}ms`);

    // Set up the discord bot
    client = new SnugglesClient({
        intents: [
            GatewayIntentBits.Guilds,
            GatewayIntentBits.GuildMessages,
            GatewayIntentBits.MessageContent
        ],
        partials: [
            Partials.Message,
            Partials.Channel
        ]
    });

    await client.startSnuggling(config().authorization.discord_token)
})();
