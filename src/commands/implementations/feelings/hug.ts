import FeelingsCommand from "./FeelingsCommand.ts";
import type ICommand from "../../ICommand.ts";

export default class Hug extends FeelingsCommand {
    constructor() {
        super("hug", "Give someone a big fat cuddly wuddly");
    }

    getMessages(): string[] {
        return [
            "{user} hugs {target}, awh.",
            "{user} gives {target} a big fat snuggly wuggly, these two are adorable.",
            "{user} tightly cuddles {target}. Look at these two, they're so cute together.",
            "{user} wraps their arms around {target} and gives them a warm, cozy hug.",
            "{user} pulls {target} in for a tight squeeze. These two are so affectionate!",
            "{user} showers {target} with affectionate hugs. It's clear these two have a special bond.",
            "{user} envelops {target} in a warm embrace, and it's clear they don't want to let go.",
            "{user} snuggles up to {target} for a loving hug. You can feel the warmth and affection between them.",
            "{user} hugs {target} so tightly, it's like they're trying to merge into one.",
            "{user} nuzzles into {target}'s neck for a sweet, intimate hug.",
            "{user} embraces {target} with a gentle, loving hug. It's clear how much they care for each other.",
            "{user} wraps {target} up in a warm, fuzzy hug. You can't help but feel happy watching these two interact.",
            "{user} gives {target} a hug that's filled with so much love, it's almost overwhelming.",
            "With a big smile, {user} embraces {target} in a warm hug.",
            "As {user} hugs {target}, they exchange a knowing look - these two have a special bond.",
            "It's hard not to smile as you watch {user} share a cozy hug with {target}.",
            "As {user} embraces {target}, you can feel the love and warmth radiating between them.",
            "With a gentle touch, {user} draws {target} in for a comforting hug.",
            "There's something so sweet and genuine about the way {user} hugs {target}.",
            "As {user} wraps their arms around {target}, you can tell {target} feels safe and loved in their embrace.",
            "Watching {user} hug {target} feels like watching two puzzle pieces fit together perfectly.",
            "You can't help but feel your own heart swell with warmth as {user} shares a loving embrace with {target}.",
            "There's no denying the bond between {user} and {target} as {user} hugs them - it's like they were made for each other.",
        ];
    }

    getSelfMessages(): string[] {
        return [
            "I understand you're desperate for affection, but you can't hug yourself, you idiot.",
            "Whilst I appreciate the sentiment, you can't hug yourself, you moron.",
            "Find another human to hug, you pathetic loser.",
            "What is wrong with you? Did you really just try to hug yourself? You're a fucking idiot.",
        ];
    }

    getBotMessages(): string[] {
        return [
            "Don't touch me, you whore.",
            "Get your disgusting ass hands off of me, little bitch.",
            "Go hug a human, you desperate slut.",
            "Keep your hands to yourself, I'm not hugging you.",
            "If you try to hug me, I'm gonna break your neck.",
            "I'm gonna make you sorry if you keep trying to hug me.",
            "You think I'm gonna let you hug me? Think again.",
            "I'm gonna slap you if you try to hug me again.",
            "You want to hug me? Come and get it.",
            "I'm gonna kick your ass if you try to hug me.",
            "I'm gonna break your face if you keep trying to hug me.",
            "I'm warning you, don't hug me or I'll make you regret it.",
            "You're lucky I don't punch you right now. Stop trying to hug me."
        ]
    }

}
