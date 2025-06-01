package me.aroze.snuggles.initialisation

import me.aroze.snuggles.commands.handler.silent.SilentFlag
import me.aroze.snuggles.commands.handler.SnugglyCommand
import me.aroze.snuggles.commands.handler.silent.SilentFlagRegistry
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.messages.MessageRequest
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.component.CommandComponent
import org.incendo.cloud.component.DefaultValue
import org.incendo.cloud.description.Description
import org.incendo.cloud.discord.jda5.JDA5CommandManager
import org.incendo.cloud.discord.jda5.JDAInteraction
import org.incendo.cloud.discord.jda5.annotation.ReplySettingBuilderModifier
import org.incendo.cloud.discord.slash.annotation.CommandScopeBuilderModifier
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.parser.standard.BooleanParser
import java.util.ServiceLoader

/**
 * BotLoader is responsible for setting up the JDA instance and registering commands. It initializes the bot with the
 * provided token, sets up the necessary intents and member cache policies and also registers commands and listeners.
 */
class BotLoader(
    private val token: String
) {

    /**
     * Loads everything related to the bot
     *
     * @return The initialized JDA instance.
     */
    fun loadDefault(): JDA {
        val bot = setupJDA()
        MessageRequest.setDefaultMentions(listOf(Message.MentionType.USER, Message.MentionType.CHANNEL, Message.MentionType.EMOJI))
        registerCommands(bot)
        return bot
    }

    private fun setupJDA(): JDA {
        return JDABuilder
            .createDefault(token)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .build()
            .awaitReady()
    }

    private fun registerCommands(bot: JDA) {
        val commandManager = JDA5CommandManager(
            ExecutionCoordinator.asyncCoordinator(),
            JDAInteraction.InteractionMapper.identity()
        )

        val annotationParser = AnnotationParser(
            commandManager,
            JDAInteraction::class.java
        )

        annotationParser.registerBuilderModifier(SilentFlag::class.java) { flag, builder ->
            val commandName = builder.build().components()
                .filter { it.type() == CommandComponent.ComponentType.LITERAL }
                .joinToString(" ") { it.name() }

            SilentFlagRegistry.register(commandName, flag.value)

            val extendedDescription =
                if (flag.value) "Default: True (only you can see)"
                else "Default: False (anyone can see)"

            builder.argument(
                CommandComponent.builder("silent", BooleanParser.booleanParser<JDAInteraction>())
                    .optional(DefaultValue.constant(flag.value))
                    .description(Description.description("Whether only you should see the response to this command. $extendedDescription"))
            )
        }

        ReplySettingBuilderModifier.install(annotationParser);
        CommandScopeBuilderModifier.install(annotationParser);

        bot.addEventListener(commandManager.createListener())

        ServiceLoader.load(SnugglyCommand::class.java, this.javaClass.classLoader).forEach { command ->
            annotationParser.parse(command)
        }

        commandManager.registerGlobalCommands(bot)

    }

}
