import {Events, type ClientEvents} from "discord.js";
import {Logger, type ILogObj} from "tslog";
import type IEvent from "../IEvent.ts";
import {deployCommands} from "../../utils/restUtils.ts";
import SnugglesClient from "../../structure/Client.ts";

export const logger: Logger<ILogObj> = new Logger();

export class ClientReadyHandler implements IEvent {
  public event: keyof ClientEvents = Events.ClientReady
  public once = true;

  constructor(private client: SnugglesClient) {}

  async handle() {
    logger.info(`Logged in as ${this.client.user?.tag} (${this.client.user?.id})`);

    // TODO: Config to determine if we should deploy commands
    await deployCommands(this.client, this.client.commands, '1246242269154246796');
  }
}