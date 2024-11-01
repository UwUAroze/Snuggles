import FeelingsCommand from "./FeelingsCommand.ts";

export default class Poke extends FeelingsCommand {
    constructor() {
        super("poke", "Pokes your victim");
    }

    getMessages(): string[] {
        return [
            "{user} playfully pokes {target} with a grin on their face.",
            "{user} pokes {target} and laughs, clearly enjoying the playful moment.",
            "With a mischievous glint in their eye, {user} pokes {target} and tries to stifle a giggle.",
            "{user} gently pokes {target} on the arm, enjoying the light-hearted moment.",
            "With a quick poke, {user} gets {target}'s attention and flashes them a smile.",
            "{user} reaches out and pokes {target}, just trying to add a bit of feelings to the day.",
            "As {user} pokes {target}, they share a laugh and enjoy the moment of levity.",
            "With a playful poke, {user} lets {target} know they're thinking of them.",
            "As {user} pokes {target} on the side, they can't help but laugh at the silly moment.",
            "With a poke on the shoulder, {user} reminds {target} that they're always there for them.",
            "{user} pokes {target} and makes a silly face, clearly in the mood for some feelings.",
            "As {user} pokes {target}, they break out into a playful dance, drawing laughter from everyone around.",
            "With a ticklish poke, {user} sends {target} into a fit of giggles.",
            "With a playful poke, {user} teases {target} and invites them to join in on the feelings.",
            "{user} pokes {target} and then runs away, daring them to try and catch them.",
            "With a quick poke and a wink, {user} hints to {target} at the playful shenanigans to come.",
            "As {user} pokes {target} more and more, they both burst into uncontrollable laughter.",
            "With a series of playful pokes, {user} and {target} engage in a lighthearted game of poking tag.",
            "{user} pokes {target} and then jumps back, ready for a round of playful banter.",
            "With a silly voice and a playful poke, {user} reminds {target} that life's too short to be serious all the time."
        ];
    }

    getSelfMessages(): string[] {
        return [
            "Awwwh you wanna poke yourself? That's so cute.",
            "Since you get 0 joes, you've settled for poking yourself. Honestly, that's so sad.",
            "bwo twied poking themsewves, yikie wikies.",
            "Find someone else, you maidenless, lonely bitch.",
            "Go play with yourself elsewhere, that's so gross.",
        ];
    }

    getBotMessages(): string[] {
        return [
            "Aint nobody poking me.",
            "Eat shit, stub your toe and cry.",
            "Did you really think I wouldn't notice you trying to poke me? You're fucking pathetic.",
            "I'm not a toy, back the fuck off.",
            "Ahah. hahhaha. haha. hah. I'm gonna poke YOU!",
            "You're a fucking idiot, you know that? You can't poke me, you moron.",
            "Poke me one more time and I'm gonna fucking kill you.",
            "You try to poke me again and I'm gonna break your fingers.",
            "What are you, some kind of masochist? Stop poking me.",
            "If you touch me again, I'm gonna make you regret it.",
            "I'll break your arm if you try to poke me again.",
            "You're lucky I don't punch you right now. Stop poking me.",
            "I'm gonna make you sorry if you keep poking me.",
            "You want to poke me? Come get some.",
            "I'm gonna break your face if you try to poke me again.",
            "I'm warning you, stop poking me or you're gonna regret it."
        ];
    }
}
