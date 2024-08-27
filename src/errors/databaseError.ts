import { logger } from "..";

export class DBError extends Error {
  constructor() {
    logger.error(
      "There was an error while connecting to the database. Is your MongoDB URI correct?"
    );
    super(
      "There was an error while connecting to the database. Is your MongoDB URI correct?"
    );
    this.name = "DBError";
    this.stack = (<any>new Error()).stack;
  }
}
