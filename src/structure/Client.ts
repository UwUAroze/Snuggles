import {Client, type ClientOptions} from "discord.js";
import {ClientReadyHandler} from "../events/handlers/ClientReadyHandler.ts";
import {InteractionCreateHandler} from "../events/handlers/InteractionCreateHandler.ts";
import Ping from "../commands/implementations/utility/ping.ts";
import type IEvent from "../events/IEvent.ts";
import Hug from "../commands/implementations/feelings/hug.ts";
import Lick from "../commands/implementations/feelings/lick.ts";
import Poke from "../commands/implementations/feelings/poke.ts";
import Slap from "../commands/implementations/feelings/slap.ts";
import Yell from "../commands/implementations/feelings/yell.ts";
import Ship from "../commands/implementations/fun/ship.ts";
import {CountingHandler} from "../events/handlers/CountingHandler.ts";

export default class SnugglesClient extends Client {
  public events: (new (client: SnugglesClient) => IEvent)[] = [
    ClientReadyHandler,
    InteractionCreateHandler,
    CountingHandler
  ];

  public commands = [
      // Utility commands
      new Ping(),

      // Feeling commands
      new Hug(),
      new Lick(),
      new Poke(),
      new Slap(),
      new Yell(),

      // Fun commands
      new Ship()
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
