import FeelingsCommand from "./FeelingsCommand.ts";

export default class Lick extends FeelingsCommand {
    constructor() {
        super("lick", "Use this to lick your prey ;)");
    }

    getMessages(): string[] {
        return [
            "{user} sneakily licks {target}'s nose, so adorable!",
            "{user} affectionately licks {target}'s face, what a sweetie!",
            "{user} curiously licks {target}'s ear, how silly!",
            "{user} gives {target} a friendly lick, making everyone in the room feel awkward!",
            "{user} slobbers all over {target}'s face, proving that dogs really are man's best friend.",
            "{user} licks {target} as a sign of affection, or maybe just because they taste like bacon.",
            "{user} gives {target} a loving lick, leaving a slobbery surprise that will make them think twice before leaving food out again.",
            "{user} playfully licks {target}, proving that sometimes the best way to show love is through a wet, sloppy lick.",
            "{user} enthusiastically licks {target}, as if they were a tasty popsicle on a hot summer day.",
            "{user} licks {target} like it's their job, leaving them wondering if they should be paying for this kind of service.",
            "{user} gives {target} a big, slobbery lick that makes them feel like they just got slimed by a giant snail.",
            "{user} licks {target} so much that they start to wonder if they've been mistaken for a giant lollipop.",
            "{user} licks {target} as if their life depends on it, which is a good thing, because it's the only way they'll get their daily dose of affection.",
            "{user} licks {target} with such enthusiasm, it's like they're trying to give them a full-body bath with their tongue.",
            "{user} takes licking {target} to a whole new level, proving that they're the ultimate multitasker by simultaneously giving lickes and cleaning up spills.",
            "{user} licks {target} as if they were a toad, which is both adorable and a little bit gross at the same time.",
            "{user} licks {target} so much, they start to feel like they're part of a bizarre ASMR video.",
            "{user} gives {target} a few playful licks, reminding them that sometimes the best things in life are a little bit messy.",
            "{target} giggles as {user} licks their nose, tickling them in the process.",
            "{user} gives {target} a gentle lick on the hand, showing their appreciation for a good scratch.",
            "{target} can't help but smile as {user} gives them a loving lick on the cheek.",
            "{user} licks {target}'s forehead, leaving behind a wet spot and a feeling of warmth.",
            "{target} laughs as {user} playfully licks their toes, tickling them in the process.",
            "{user} gives {target} a few affectionate licks on the neck, showing their love in a unique way.",
            "{target} feels loved and comforted as {user} licks their hair, almost like a grooming ritual.",
            "{user} licks {target}'s arm, leaving behind a trail of slobber and a feeling of closeness.",
            "{target} playfully scolds {user} for giving them a slobbery lick on the nose, but secretly loves the attention.",
            "{user} gives {target} a few playful licks on the ear, making them feel like they have a furry little companion.",
        ];
    }

    getSelfMessages(): string[] {
        return [
            "Awwwh, you wanna lick yourself? That's so cute.",
            "Licking yourself is kinda weirddd, find someone else to smear your saliva on!",
            "Stop licking yourself, go put your so called brain to use.",
            "Woah there, what are you doing.",
            "Shouldd I let you lick yourselff? Hmmm, mmmm, no.",
        ];
    }

    getBotMessages(): string[] {
        return [
            "Your breath stinks, get it the fuck away from me.",
            "Aint no way bro just tried to lick me.",
            "Do you have a licking kink?? Aw, cute. But I'm not the one. Sorry, not sorry.",
            "Go lick yourself.",
            "Go lick someone else, or I'm gonna start licking you. Jk, you wish. You desperate little bitch.",
            "You lick me one more time and I'm gonna kick your ass.",
            "I'm gonna sue you if you keep licking me.",
            "Get the fuck away from me, you little pervert.",
            "If you come near me with your tongue again, I'm gonna punch you.",
            "You can just forget about licking me, it ain't gonna happen.",
            "I'm gonna kick your ass if you try to lick me again.",
            "What the hell is wrong with you? Stop licking me!",
            "I'm gonna call the police if you don't stop licking me.",
            "I swear to God, if you don't stop licking me I'm gonna fight you.",
            "You want a licking? I'll give you one you won't forget."
        ];
    }
}
