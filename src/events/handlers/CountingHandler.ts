import {Events, type ClientEvents, Message, type OmitPartialGroupDMChannel} from "discord.js";
import {Logger, type ILogObj} from "tslog";
import type IEvent from "../IEvent.ts";
import {deployCommands} from "../../utils/restUtils.ts";
import type SnugglesClient from "../../structure/Client.ts";

export const logger: Logger<ILogObj> = new Logger();

export class CountingHandler implements IEvent {
  public event: keyof ClientEvents = Events.MessageCreate;
  public once = false;

  constructor(private client: SnugglesClient) {}

  async handle(message: OmitPartialGroupDMChannel<Message<boolean>>) {
    logger.debug(`Message received: ${message.content}`);
  }
}
