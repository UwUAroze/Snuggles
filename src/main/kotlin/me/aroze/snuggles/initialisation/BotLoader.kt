package me.aroze.snuggles.initialisation

import me.aroze.snuggles.commands.handler.silent.SilentFlag
import me.aroze.snuggles.commands.SnugglyCommand
import me.aroze.snuggles.commands.handler.silent.SilentFlagManager
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


class BotLoader(
    private val token: String
) {

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

            SilentFlagManager.register(commandName, flag.value)

            builder.argument(
                CommandComponent.builder("silent", BooleanParser.booleanParser<JDAInteraction>())
                    .optional(DefaultValue.constant(flag.value))
                    .description(Description.description("Whether or not to send the message publicly. Default: ${flag.value}"))
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
