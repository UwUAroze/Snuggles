import type {User} from "discord.js";

/**
 * Formats a number with commas
 *
 * @param number The number to format
 */
export function formatCommas(number: number): string {
    return number.toLocaleString('en-US');
}

/**
 * Formats a user as a mention
 *
 * @param user The user to mention
 */
export function userAsMention(user: User): string {
    return `<@${user.id}>`;
}
