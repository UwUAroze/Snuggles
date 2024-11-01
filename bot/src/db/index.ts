import { drizzle } from "drizzle-orm/node-postgres";
import { Pool } from "pg";
import config from "../config/config.ts";

const pool = new Pool({
    connectionString: config().database.connection_string
})

export const db = drizzle(pool);
