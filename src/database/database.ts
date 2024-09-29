// TODO: Switch to postgres w/ drizzle
// import { connect, disconnect, connection } from "mongoose";
//
// export async function databaseConnect(mongoUri: string): Promise<void> {
//   await connect(mongoUri);
// }
//
// export async function databaseDisconnect(): Promise<void> {
//   await disconnect();
// }
//
// export async function databaseLatency(): Promise<number> {
//   const start = Date.now();
//   if (!connection.db) throw new Error("No database connection");
//   await connection.db.commands({ping: 1});
//   return Date.now() - start;
// }
