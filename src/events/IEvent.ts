import type {ClientEvents} from "discord.js";

/**
 * Represents an event that can be listened for by the bot
 * @interface IEvent
 * @property {keyof ClientEvents} event - The name of the event
 * @property {boolean} once - Whether the event should only be handled once
 * @property {(...args: any[]) => Promise<void>} handle - The function that handles the event
 */
export default interface IEvent {
    event: keyof ClientEvents;
    once: boolean;
    handle(...args: any[]): Promise<void>;
}
