import { connect, disconnect, connection } from "mongoose";
import { DBError } from "../errors/databaseError";

export async function databaseConnect(mongoUri: string): Promise<void> {
  try {
    await connect(mongoUri);
  } catch {
    throw new DBError();
  }
}

export async function databaseDisconnect(): Promise<void> {
  await disconnect();
}

export async function databaseLatency(): Promise<number> {
  const start = Date.now();
  await connection.db.command({ ping: 1 });
  return Date.now() - start;
}
