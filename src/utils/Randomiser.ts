export default class Randomiser<T> {

    shuffled: T[];
    currentIndex = 0;

    constructor(list: T[]) {
        this.shuffled = shuffle(list);
    }

    next(): T {
        if (this.currentIndex >= this.shuffled.length) {
            this.shuffled = shuffle(this.shuffled);
            this.currentIndex = 0;
        }

        return this.shuffled[this.currentIndex++];
        }

}

export function shuffle<T>(array: T[]): T[] {
    const shuffledArray = [...array];

    for (let i = shuffledArray.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [shuffledArray[i], shuffledArray[j]] = [shuffledArray[j], shuffledArray[i]];
    }

    return shuffledArray;
}
