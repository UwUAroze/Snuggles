package me.aroze.snuggles.commands.impl.channel.counting

import instance
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.arozeutils.kotlin.extension.cutOff
import me.aroze.snuggles.models.CountData
import me.aroze.snuggles.utils.FancyEmbed
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import java.util.concurrent.CompletableFuture

object CountingUI {
    fun createGuildUI(guild: String? = null): CompletableFuture<Pair<EmbedBuilder, List<ItemComponent>>> = runBlocking {
        val future = CompletableFuture<Pair<EmbedBuilder, List<ItemComponent>>>()

        launch {
            val eb = FancyEmbed()
                .setTitle("Counting Leaderboards (This guild)")
                .setFooter("Psst: These stats are cached for upto 5 minutes!!")

            val topUsers = CountData.getTopUsers(guild)
            val description = StringBuilder()

            for ((i, user) in topUsers.withIndex()) description.append("> ${i+1}) ${instance.retrieveUserById(user._id).complete().asTag.cutOff(30)} - ${user.totalCounts} counts\n")
            description.append("ㅤ")

            eb.setDescription(description.toString())

            val buttons = listOf(
                Button.primary("counting-guild", "Guild Leaderboards")
                    .withEmoji(Emoji.fromFormatted("<:trophy:1085598525116649612>")).withDisabled(true),
                Button.secondary("counting-global", "Global Leaderboards")
                    .withEmoji(Emoji.fromFormatted("<:goofyahhworld:1085588511815643216>"))
            )

            future.complete(Pair(eb, buttons))
        }
        return@runBlocking future
    }

    fun createGlobalUI(): CompletableFuture<Pair<EmbedBuilder, List<ItemComponent>>> = runBlocking {
        val future = CompletableFuture<Pair<EmbedBuilder, List<ItemComponent>>>()

        launch {
            val eb = FancyEmbed()
                .setFooter("Psst: These stats are cached for upto 5 minutes!!")

            val usersField = StringBuilder()
            val guildsField = StringBuilder()

            val topUsers = CountData.getTopUsers()
            for ((i, user) in topUsers.withIndex()) {
                val retrievedUser = instance.retrieveUserById(user._id).complete()
                usersField.append(" > ${i+1}) ${retrievedUser.name.cutOff(10)}#${retrievedUser.discriminator} - ${user.totalCounts} counts\n")
            }
            usersField.append("ㅤ")

            val topGuildsByCounts = CountData.getTopGuildsByCounts()
            for ((i, guild) in topGuildsByCounts.withIndex()) guildsField.append("> ${i+1}) ${instance.getGuildById(guild._id)?.name?.cutOff(15) ?: "Unknown Guild"} - ${guild.totalCounts} counts\n")

            guildsField.append("\n**Top Guilds (Highest ever score)**")

            val topGuildsByHighscores = CountData.getTopGuildsByHighScores()
            guildsField.append("\n")
            for ((i, guild) in topGuildsByHighscores.withIndex()) guildsField.append("> ${i+1}) ${instance.getGuildById(guild._id)?.name?.cutOff(15) ?: "Unknown Guild"} - ${guild.highScore} counts\n")
            guildsField.append("ㅤ")

            eb.setTitle("Counting Leaderboards (Global)")
            eb.addField("Top Users", usersField.toString(), true)
            eb.addField("Top Guilds (Most Counts)", guildsField.toString(), true)

            val buttons = listOf(
                Button.secondary("counting-guild", "Guild Leaderboards")
                    .withEmoji(Emoji.fromFormatted("<:trophy:1085598525116649612>")),
                Button.primary("counting-global", "Global Leaderboards")
                    .withEmoji(Emoji.fromFormatted("<:goofyahhworld:1085588511815643216>")).withDisabled(true)
            )

            future.complete(Pair(eb, buttons))

        }
        return@runBlocking future
    }

}