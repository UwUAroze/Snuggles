package me.aroze.snuggles.commands

enum class Feelings(val feeling: String, val description: String, val messages: List<String>, val self: List<String> = listOf(), val bot: List<String> = listOf()) {
    HUG("hug", "Hugs a user", listOf(
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
    ),
        listOf(
            "I understand you're desperate for affection, but you can't hug yourself, you idiot.",
            "Whilst I appreciate the sentiment, you can't hug yourself, you moron.",
            "Find another human to hug, you pathetic loser.",
            "What is wrong with you? Did you really just try to hug yourself? You're a fucking idiot.",
        ),
        listOf(
            "Don't touch me, you whore.",
            "Get your disgusting ass hands off of me, little bitch.",
            "Go hug a human, you desperate slut."
        )
    ),

    POKE("poke", "Pokes your victim", listOf(
        "{user} playfully pokes {target} with a grin on their face.",
        "{user} pokes {target} and laughs, clearly enjoying the playful moment.",
        "With a mischievous glint in their eye, {user} pokes {target} and tries to stifle a giggle.",
        "{user} gently pokes {target} on the arm, enjoying the light-hearted moment.",
        "With a quick poke, {user} gets {target}'s attention and flashes them a smile.",
        "{user} reaches out and pokes {target}, just trying to add a bit of fun to the day.",
        "As {user} pokes {target}, they share a laugh and enjoy the moment of levity.",
        "With a playful poke, {user} lets {target} know they're thinking of them.",
        "As {user} pokes {target} on the side, they can't help but laugh at the silly moment.",
        "With a poke on the shoulder, {user} reminds {target} that they're always there for them.",
        "{user} pokes {target} and makes a silly face, clearly in the mood for some fun.",
        "As {user} pokes {target}, they break out into a playful dance, drawing laughter from everyone around.",
        "With a ticklish poke, {user} sends {target} into a fit of giggles.",
        "With a playful poke, {user} teases {target} and invites them to join in on the fun.",
        "{user} pokes {target} and then runs away, daring them to try and catch them.",
        "With a quick poke and a wink, {user} hints at the playful shenanigans to come.",
        "As {user} pokes {target} more and more, they both burst into uncontrollable laughter.",
        "With a series of playful pokes, {user} and {target} engage in a lighthearted game of poking tag.",
        "{user} pokes {target} and then jumps back, ready for a round of playful banter.",
        "With a silly voice and a playful poke, {user} reminds {target} that life's too short to be serious all the time."
    ),
        listOf(
            "Awwwh you wanna poke yourself? That's so cute.",
            "Since you get 0 bitches, you've settled for poking yourself, honestly that's so sad.",
            "Yikes, mf tried poking themselves. Find someone else, you maidenless, lonely bitch.",
            "Go play with yourself elsewhere, that's so gross.",
        ),
        listOf(
            "Aint nobody poking me. Eat shit, stub your toe and cry.",
            "Did you really think I wouldn't notice you trying to poke me? You're fucking pathetic.",
            "I'm not a toy, back the fuck off.",
            "Ahah. hahhaha. haha. hah. I'm gonna poke YOU!",
            "You're a fucking idiot, you know that? You can't poke me, you moron.",
        )
    ),

    LICK("lick", "Use this to lick your prey ;)", listOf(
        "{user} sneakily licks {target}'s nose, so adorable!",
        "{user} affectionately licks {target}'s face, what a sweetie!",
        "{user} curiously licks {target}'s ear, how silly!",
        "{user} gives {target} a friendly lick, making everyone in the room feel awkward!",
        "{user} slobbers all over {target}'s face, proving that dogs really are man's best friend.",
        "{user} licks {target} as a sign of affection, or maybe just because they taste like bacon.",
        "{user} gives {target} a loving lick, leaving a slobbery surprise that will make them think twice before leaving food out again.",
        "{user} playfully licks {target}, proving that sometimes the best way to show love is through a wet, sloppy kiss.",
        "{user} enthusiastically licks {target}, as if they were a tasty popsicle on a hot summer day.",
        "{user} licks {target} like it's their job, leaving them wondering if they should be paying for this kind of service.",
        "{user} gives {target} a big, slobbery kiss that makes them feel like they just got slimed by a giant snail.",
        "{user} licks {target} so much that they start to wonder if they've been mistaken for a giant lollipop.",
        "{user} licks {target} as if their life depends on it, which is a good thing, because it's the only way they'll get their daily dose of affection.",
        "{user} licks {target} with such enthusiasm, it's like they're trying to give them a full-body bath with their tongue.",
        "{user} takes licking {target} to a whole new level, proving that they're the ultimate multitasker by simultaneously giving kisses and cleaning up spills.",
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
    ),
        listOf(
            "Awwwh, you wanna lick yourself? That's so cute.",
            "Licking yourself is kinda weirddd, find someone else to smear your saliva on!",
            "Stop licking yourself, go put your so called brain to use.",
            "Woah there, what are you doing.",
            "Shouldd I let you lick yourselff? Hmmm, mmmm, no.",
        ),
        listOf(
            "Your breath stinks, get it the fuck away from me.",
            "Aint no way bro just tried to lick me.",
            "Do you have a licking kink?? Aw, cute. But I'm not the one. Sorry, not sorry.",
            "Go lick yourself.",
            "Go lick someone else, or I'm gonna start licking you. Jk, you wish. You desperate little bitch."
        )
    ),

    // TODO: Slap, high five, yell, facepalm, bite, shake, stab, murder, dab, cry, pat

    // Template:

    //    ACTION("action", "whatever", listOf(
    //        ""
    //    ),
    //        listOf(),
    //        listOf()
    //    )

}