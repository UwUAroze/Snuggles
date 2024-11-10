import toml from "@iarna/toml";
import fs from "node:fs";
import {logger} from "../index";

interface Config {
    authorization: {
        discord_token: string
    },
    database: {
        mongo_uri: string,
        database_name: string
    }
    commands: {
        global: boolean
        deploy: boolean
        guild_id: string
    },
}

const exampleConfig: Config = {
    authorization: {
        discord_token: ""
    },
    database: {
        mongo_uri: "",
        database_name: ""
    },
    commands: {
        global: false,
        deploy: true,
        guild_id: ""
    }
}

let loadedConfig: Config;

export default function config(): Config {
    if (!loadedConfig) {
        let now = new Date();

        if (!fs.existsSync("config.toml")) {
            fs.writeFileSync("config.toml", toml.stringify(exampleConfig as any));
        }

        const configData = fs.readFileSync("config.toml").toString()
        loadedConfig = toml.parse(configData) as any as Config;
        logger.info(`Loaded config in ${new Date().getTime() - now.getTime()}ms`);
    }

    return loadedConfig;
}
