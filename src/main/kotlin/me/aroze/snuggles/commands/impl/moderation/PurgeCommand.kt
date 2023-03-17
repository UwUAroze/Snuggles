package me.aroze.snuggles.commands.impl.moderation

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import me.aroze.snuggles.commands.handler.Constraint
import me.aroze.snuggles.commands.handler.Description
import me.aroze.snuggles.utils.FancyEmbed
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member

@Command(
    description = "Deletes all messages that match the specified filter",
    permissions = [Permission.MESSAGE_MANAGE, Permission.MANAGE_CHANNEL],
)
class PurgeCommand {
    fun main(
        event: CommandEvent,
        @Description("The amount of messages to check through and potentially delete") @Constraint(5, 100) amount: Int,
        @Description("Option to only purge messages from a specified user") user: Member?,
        @Description("Only purges messages containing this") filter: String?
    ) = runBlocking {
        launch {
            val messages = event.message.channel.history.retrievePast(amount).complete()
            val filtered = messages.filter {
                (user == null || it.author.id == user.id) && (filter == null || it.contentRaw.contains(filter))
            }

            event.message.channel.purgeMessages(filtered)

            val eb = FancyEmbed()

            if (filtered.isEmpty()) {
                eb.setAuthor(
                    "No messages matched the filter ;c",
                    null,
                    "https://cdn.discordapp.com/emojis/1086030825793003580.webp?size=96&quality=lossless"
                )
            } else {
                eb.setAuthor(
                    "${filtered.size} messages matching the filter have been purged",
                    null,
                    "https://cdn.discordapp.com/emojis/1086030825793003580.webp?size=96&quality=lossless"
                )

                event.message.replyEmbeds(eb.build()).queue()

            }
        }
    }
}