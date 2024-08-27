import { logger } from "..";

export class EnvError extends Error {
  constructor(msg: EnvErrType) {
    logger.error(msg);
    super(msg);
    this.name = "EnvError";
    this.stack = (<any>new Error()).stack;
  }
}

/** Error types for envError */
export enum EnvErrType {
  NO_DISCORD_TOKEN = "No Discord token was provided. Provide it in .env",
  NO_MONGO_URI = "No MongoDB URI provided. Provide it in .env",
}
