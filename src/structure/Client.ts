import {Client, type ClientOptions} from "discord.js";
import {ClientReadyHandler} from "../events/handlers/ClientReadyHandler.ts";
import {InteractionCreateHandler} from "../events/handlers/InteractionCreateHandler.ts";
import Ping from "../commands/implementations/utility/ping.ts";
import type IEvent from "../events/IEvent.ts";
import Hug from "../commands/implementations/fun/hug.ts";
import Lick from "../commands/implementations/fun/lick.ts";
import Poke from "../commands/implementations/fun/poke.ts";
import Slap from "../commands/implementations/fun/slap.ts";
import Yell from "../commands/implementations/fun/yell.ts";
import Ship from "../commands/implementations/fun/ship.ts";

export default class SnugglesClient extends Client {
  public events: (new (client: SnugglesClient) => IEvent)[] = [
    ClientReadyHandler,
    InteractionCreateHandler
  ];

  public commands = [
      // Utility commands
      new Ping(),

      // Fun commands
      new Hug(),
      new Lick(),
      new Poke(),
      new Slap(),
      new Yell(),
      new Ship(),
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