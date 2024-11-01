import FeelingsCommand from "./FeelingsCommand.ts";

export default class Slap extends FeelingsCommand {
    constructor() {
        super("slap", "Slaps your victim");
    }

    getMessages(): string[] {
        return [
            "{user} grabs {target} and slaps them so hard, it echoes through the room.",
            "{user} grabs {target}'s face and viciously slaps them with all their might.",
            "{user} throws out their hand and slaps {target} hard enough to make their ears ring.",
            "{user} delivers a harsh, violent slap to {target}'s face.",
            "{user} slaps {target} with a loud crack, leaving a deep red mark on their skin.",
            "{user} smacks {target} with a loud, sharp slap, making them yelp in pain and curse.",
            "{user} grabs {target} and sends them flying with a hard slap.",
            "{user} delivers an aggressive, brutal slap to {target}'s cheek.",
            "{user} punches {target} in the face and yells profanities at them.",
            "{user} slaps {target} with enough force to make them stumble back.",
            "{user} screams obscenities at {target} before delivering a powerful, angry slap.",
            "{user} delivers a hard, violent slap that leaves a deep red mark on {target}'s skin.",
            "{user} slaps {target} with a loud thud, leaving a deep red mark on their face.",
            "{user} knocks {target} out with a hard slap.",
            "{user} grabs {target}'s face and violently slaps them with all their strength.",
            "{user} slaps {target} hard enough to make them see stars.",
            "{user} delivers a harsh, vicious slap to {target}'s face.",
            "{user} smacks {target} with a loud, sharp slap, making them yelp in pain and swear.",
            "{user} knocks {target} off their feet with a hard slap.",
            "{user} delivers an intense, brutal slap to {target}'s cheek.",
            "{user} punches {target} hard in the face and calls them demeaning names.",
            "{user} slaps {target} with enough force to make them fall down.",
            "{user} screams expletives at {target} before delivering a fierce, angry slap.",
            "{user} delivers a hard, violent slap that leaves a burning red mark on {target}'s skin.",
            "{user} slaps {target} with a loud thud, leaving a deep red welt on their face.",
            "{user} slaps {target} so hard, it leaves them dazed.",
            "{user} brutally slaps {target}'s face with all their strength.",
            "{user} slaps {target} hard enough to make them deafen.",
            "{user} delivers a harsh, vicious slap to {target}'s face with a string of curses.",
            "{user} smacks {target} with a loud, sharp slap, making them yelp in pain and swear profusely.",
            "{user} knocks {target} unconscious with a hard slap.",
            "{user} delivers an aggressive, brutal slap to {target}'s cheek with a string of curses.",
            "{user} punches {target} in the face and shouts a slur at them.",
            "{user} slaps {target} with enough force to make them fall to their knees.",
            "{user} screams obscenities at {target} before delivering a fierce, violent slap.",
            "{user} slaps {target} with a loud crack, leaving a red mark on their cheek.",
            "{user} gives {target} a hard slap across the face.",
            "{user} slaps {target} so hard, the sound echoes through the room.",
            "{user} throws their hand out and lands a hard slap on {target}'s face.",
            "{user} grabs {target} by the collar and delivers an angry slap to their face.",
            "{user} slaps {target} with so much force, it sends them reeling.",
            "{user} grabs {target}'s face and slaps them with all their might.",
            "{user} delivers an aggressive slap to {target}'s cheek, followed by a stern look.",
            "{user} smacks {target} with a loud slap, making them yelp in pain.",
            "{target} recoils in shock as {user} slaps them hard.",
            "{user} delivers a stinging slap to {target}'s face.",
            "{user} makes a loud, sharp sound as they slap {target}.",
            "{target} cries out in pain as {user} violently slaps them.",
            "{user} quickly throws their hand out and slaps {target} in the face.",
            "{user} grabs {target}'s collar and delivers a sharp slap to their face.",
            "{target} jumps back in surprise as {user} slaps them hard.",
            "{user} strikes {target} across the face with an open palm, leaving a searing pain in their cheek.",
            "{user} delivers a resounding slap to {target}'s cheek, causing their head to jerk to the side.",
            "{user} snaps their hand forward, slapping {target} with a force that rattles their teeth.",
            "{user} whips their hand through the air, landing a sharp slap on {target}'s face that leaves a red welt.",
            "{user} smacks {target} hard in the face, causing their eyes to water and their nose to bleed.",
            "{user} raises their hand and delivers a resolute slap to {target}'s cheek, showing their disapproval.",
            "{user} lands a swift and powerful slap to {target}'s face, causing a loud crack to echo through the room.",
            "{user} swings their hand with purpose, striking {target} on the cheek with a loud thwack.",
            "{user} sharply slaps {target} across the face, causing their head to whip around and their eyes to widen in shock.",
            "{user} delivers a firm and audible slap to {target}'s face, leaving behind a stinging sensation and a faint red mark.",
            "The sound of flesh meeting flesh echoes through the room as {user} lands a hard slap on {target}'s cheek.",
            "{user} delivers a sharp blow to {target}'s face, leaving a handprint that slowly turns red.",
            "Without warning, {user} strikes {target} across the face with an open palm, sending them stumbling backwards.",
            "In a fit of rage, {user} slaps {target} so hard that their head snaps to the side.",
            "{user} backhands {target} with a resounding crack, leaving a red mark on their cheek.",
            "With a quick, decisive motion, {user} slaps {target} across the face, leaving them dazed and disoriented.",
            "{user} smacks {target} with an open palm, causing their head to whip around.",
            "Without a word, {user} delivers a stinging slap to {target}'s face, leaving them reeling.",
            "With a look of disgust, {user} slaps {target} across the face, making them flinch.",
            "{user} lands a solid slap on {target}'s cheek, causing them to stumble backwards in shock."
        ];
    }

    getSelfMessages(): string[] {
        return [
            "Why would you even think of slapping yourself? That's just sad.",
            "You think slapping yourself will make you feel better? You're a joke.",
            "What did you do to deserve a slap? You're a fucking loser.",
            "You really want to slap yourself, you masochistic piece of shit?",
            "Go ahead and slap yourself if you want, it won't make a difference anyway.",
            "Slapping yourself won't make the pain go away, you fucking moron.",
            "What, you think slapping yourself will help? You're an idiot.",
            "Slapping yourself, are you trying to hurt yourself? You're a fucking loser.",
            "You're so desperate for attention you're trying to slap yourself? That's pathetic.",
            "Go ahead and slap yourself, it won't hurt any more than your feelings already are."
        ];
    }

    getBotMessages(): string[] {
        return [
            "I dare you to try and slap me. Just try it, I dare you.",
            "What the fuck? You really think you can just come up and slap me like that?",
            "You want a slap? Come get it.",
            "You want to slap me, huh? Let's go!",
            "I'm gonna slap you so hard you'll be seeing stars for weeks.",
            "You're lucky I don't slap you right now, you little shit.",
            "I'm gonna knock you the fuck out if you ever try to slap me again.",
            "I'm not a punching bag, back off before I slap you!",
            "You even think about slapping me and I'll fucking end you.",
            "Try it and see what happens. I promise you won't like it."
        ];
    }
}
