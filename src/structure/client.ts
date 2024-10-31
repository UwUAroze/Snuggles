import {Client, type ClientOptions} from "discord.js";
import {ClientReadyEventHandler} from "../events/implementation/clientReadyEventHandler.ts";
import {InteractionCreateEventHandler} from "../events/implementation/interactionCreateEventHandler.ts";
import PingCommand from "../commands/implementation/utility/pingCommand.ts";
import type IEvent from "../events/IEvent.ts";
import HugCommand from "../commands/implementation/fun/hugCommand.ts";
import LickCommand from "../commands/implementation/fun/lickCommand.ts";
import PokeCommand from "../commands/implementation/fun/pokeCommand.ts";
import SlapCommand from "../commands/implementation/fun/slapCommand.ts";
import YellCommand from "../commands/implementation/fun/yellCommand.ts";

export default class SnugglesClient extends Client {
  public events: (new (client: SnugglesClient) => IEvent)[] = [
    ClientReadyEventHandler,
    InteractionCreateEventHandler
  ];

  public commands = [
      // Utility commands
      new PingCommand(),

      // Fun commands
      new HugCommand(),
      new LickCommand(),
      new PokeCommand(),
      new SlapCommand(),
      new YellCommand(),
  ];

  constructor(options: ClientOptions) {
    super(options);
  }

  override async login(token?: string) {
    await this.registerEvents();
    return super.login(token);
  }

  private async registerEvents() {
    for (const Event of this.events) {
      const eventInstance = new Event(this);
      const eventName = eventInstance.event;
      const handler = eventInstance.handle.bind(eventInstance);
      if (eventInstance.once) {
        this.once(eventName, handler);
      } else {
        this.on(eventName, handler);
      }
    }
  }
}