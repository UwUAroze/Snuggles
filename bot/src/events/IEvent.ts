import type {ClientEvents} from "discord.js";

/**
 * Represents an events that can be listened for by the bot
 * @interface IEvent
 * @property {keyof ClientEvents} event - The name of the events
 * @property {boolean} once - Whether the events should only be handled once
 * @property {(...args: any[]) => Promise<void>} handle - The function that handles the events
 */
export default interface IEvent {
    event: keyof ClientEvents;
    once: boolean;
    handle(...args: any[]): Promise<void>;
}
