import { logger } from "../index";
import { config } from "dotenv";
import { EnvError, EnvErrType } from "../errors/envError";

interface ConfigType {
  authorization: {
    discord_token: string;
  };
  database: {
    mongo_uri: string;
    database_name: string;
  };
  commands: {
    global: boolean;
    deploy: boolean;
    guild_id: string;
  };
}

const exampleConfig: ConfigType = {
  authorization: {
    discord_token: "",
  },
  database: {
    mongo_uri: "",
    database_name: "",
  },
  commands: {
    global: false,
    deploy: true,
    guild_id: "",
  },
};

let loadedConfig: ConfigType;

export default function initalizeConfig(): ConfigType {
  if (!loadedConfig) {
    let now = new Date();

    // Load .env file
    config();

    // if (!fs.existsSync("config.toml")) {
    //     fs.writeFileSync("config.toml", toml.stringify(exampleConfig as any));
    // }
    // This is a bad practice ^^^ For example: Beginner user doesn't know why xyz which requires Mongo doesn't work.. because we are using a starter config.
    //
    // Old TOML stuff:
    // const configData = fs.readFileSync("config.toml").toString()
    // loadedConfig = toml.parse(configData) as any as ConfigType;

    loadedConfig = grabConfig();

    logger.info(`Loaded config in ${new Date().getTime() - now.getTime()}ms`);
  }

  return loadedConfig;
}

/**
 * Requires config() from `dotenv` to be ran
 * @param override Override an error if needed
 */
function grabConfig(override?: Boolean): ConfigType {
  const errorOverride = process.env.OVERRIDE_ERRORS == "true" || override;

  // Load MongoDB URI
  const mongoDBURI = process.env.MONGO;
  if ((mongoDBURI == undefined || mongoDBURI == "") && !errorOverride)
    throw new EnvError(EnvErrType.NO_MONGO_URI);

  // Load Discord token
  const discordToken = process.env.DISCORD_TOKEN;
  if ((discordToken == undefined || discordToken == "") && !errorOverride)
    throw new EnvError(EnvErrType.NO_MONGO_URI);

  let databaseName = process.env.DB_NAME;

  // Override name (change if you want)
  if (databaseName == undefined || databaseName == "") databaseName = "snuggle";

  const commandObj = {
    global: asBoolean(process.env.GLOBAL) || exampleConfig.commands.global,
    deploy: asBoolean(process.env.DEPLOY) || exampleConfig.commands.deploy,
    guild_id: process.env.GUILD_ID || "",
  };
  return {
    authorization: { discord_token: <string>discordToken },
    database: { mongo_uri: <string>mongoDBURI, database_name: databaseName },
    commands: commandObj,
  };
}
function asBoolean(str: string | undefined): boolean | undefined {
  if (str == "true") return true;
  if (str == "false") return false;
  if (str == undefined) return str;
}
