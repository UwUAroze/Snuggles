import type {SnugglyStats} from "@prisma/client";
import {Client, type ClientOptions, type User} from "discord.js";
import Hug from "../commands/implementations/feelings/hug.ts";
import Lick from "../commands/implementations/feelings/lick.ts";
import Poke from "../commands/implementations/feelings/poke.ts";
import Slap from "../commands/implementations/feelings/slap.ts";
import Yell from "../commands/implementations/feelings/yell.ts";
import Minecraft from "../commands/implementations/fun/minecraft.ts";
import Ship from "../commands/implementations/fun/ship.ts";
import Evaluate from "../commands/implementations/utility/evaluate.ts";
import Ping from "../commands/implementations/utility/ping.ts";
import SnugglyStatsCommand from "../commands/implementations/utility/snugglystats.ts";
import SnugglyStatsService from "../database/services/SnugglyStatsService.ts";
import {ClientReadyHandler} from "../events/handlers/ClientReadyHandler.ts";
import {CountingHandler} from "../events/handlers/CountingHandler.ts";
import {InteractionCreateHandler} from "../events/handlers/InteractionCreateHandler.ts";
import {
    MessageCreatePreProcessor,
    MessageDeletePreProcessor,
    MessageUpdatePreProcessor
} from "../events/handlers/preprocessor/MessagePreProcessor";
import type IEvent from "../events/IEvent.ts";

export default class SnugglesClient extends Client {
    public snugglyStats!: SnugglyStats
    public owners!: { // Used for some things like snugglystats
        aroze: User;
        lily: User;
    }

    public events: (new (client: SnugglesClient) => IEvent)[] = [
        ClientReadyHandler,
        InteractionCreateHandler,
        CountingHandler,

        MessageCreatePreProcessor,
        MessageDeletePreProcessor,
        MessageUpdatePreProcessor
    ];

    public commands = [
        // Utility commands
        new Evaluate(),
        new Ping(),
        new SnugglyStatsCommand(),

        // Feeling commands
        new Hug(),
        new Lick(),
        new Poke(),
        new Slap(),
        new Yell(),

        // Fun commands
        new Minecraft(),
        new Ship()
    ];

    constructor(options: ClientOptions) {
        super(options);
    }

    public async startSnuggling(token?: string) {
        await this.login(token);
        await this.setupDataCache();
    }

    override async login(token?: string) {
        await this.registerEvents();
        return super.login(token);
    }

    public async saveCache() {
        await SnugglyStatsService.updateSnugglyStats(this.snugglyStats);
    }

    private async setupDataCache() {
        this.snugglyStats = await SnugglyStatsService.findOrCreateSnugglyStats();
        this.owners = {
            aroze: await this.users.fetch("273524398483308549"),
            lily: await this.users.fetch("712615825965711391")
        }

        if (!this.owners.aroze || !this.owners.lily) {
            throw new Error("Failed to fetch lesbians");
        }

        setInterval(() => this.saveCache(), 1000 * 30); // Save every 30 seconds
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
