// import type {Guild} from "discord.js";
//
// interface GuildSettings {
//     prefix?: string;
// }
//
// const defaultSettings: GuildSettings = {
//     prefix: "!"
// }
//
// export default class SettingsManager {
//     private database: any;
//
//     constructor(database: any) {
//         this.database = database;
//     }
//
//     set<T>(key: keyof GuildSettings, value: T){
//         this.cache.set(key, value);
//     }
//
//     get<K extends keyof GuildSettings>(key: string): K | undefined {
//         return this.cache.get(key) as K;
//     }
//
//     private async save() {
//         // TODO: Implement saving
//     }
// }
