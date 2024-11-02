/*
  Warnings:

  - You are about to drop the `User` table. If the table is not empty, all the data it contains will be lost.

*/
-- DropTable
DROP TABLE "User";

-- CreateTable
CREATE TABLE "GuildCounting" (
    "guildId" INTEGER NOT NULL,
    "channelId" INTEGER NOT NULL,
    "count" INTEGER NOT NULL DEFAULT 0,
    "highScore" INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT "GuildCounting_pkey" PRIMARY KEY ("guildId")
);

-- CreateIndex
CREATE UNIQUE INDEX "GuildCounting_channelId_key" ON "GuildCounting"("channelId");
