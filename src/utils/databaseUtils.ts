import {prisma} from "../index.ts";

export async function getDatabasePing(): Promise<number> {
    const start = Date.now();
    await prisma.$queryRaw`SELECT 'stupid ping test'`
    return Date.now() - start;
}
