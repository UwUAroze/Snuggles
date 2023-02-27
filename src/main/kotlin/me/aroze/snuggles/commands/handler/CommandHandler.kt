package me.aroze.snuggles.commands.handler

import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.utils.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import kotlin.reflect.KParameter
import kotlin.reflect.javaType

object CommandHandler {

    val commands = mutableListOf<CommandBundle>()

    fun register(vararg commands: Class<*>) {
        for (command in commands) {
            val annotation = command.getAnnotation(Command::class.java) ?: continue
            val bundle = CommandBundle(
                annotation,
                command,
                CommandData(annotation.getName(command.simpleName), annotation.description),
                command.safeConstruct() ?: continue
            )

            this.commands.add(bundle)
            Command.getMainCommand(command)?.let {
                val options = buildOptions(it, bundle.build.name)
                bundle.build.addOptions(options)
            }

            Command.getSubCommands(command).forEach { sub ->
                val subData = sub.getAnnotation(Command::class.java)
                val name = subData?.getName(sub.name) ?: sub.name
                val desc = subData?.description ?: "No description provided"

                val options = buildOptions(sub, name)
                val subBuild = SubcommandData(name, desc).addOptions(options)

                bundle.subBuilds.add(subBuild)
                bundle.build.addSubcommands(subBuild)
            }
        }
    }

    fun load(bot: JDA) {
        bot.updateCommands().addCommands(commands.map { it.build }).queue()
        bot.addEventListener(object : ListenerAdapter() {
            override fun onSlashCommand(event: SlashCommandEvent) {
                execute(event)
            }
        })
    }

    fun execute(event: SlashCommandEvent) {
        val command = commands.find { it.build.name == event.name } ?: return
        val isDev = ConfigLoader.config.getList<String>("developers.ids").contains(event.user.id)
        val eventBundle = CommandEvent(event)

        val execution: Method = if (event.subcommandName != null) {
            Command.getSubCommands(command.clazz).find { it.name == event.subcommandName } ?: return
        } else Command.getMainCommand(command.clazz) ?: return

        val data = execution.getAnnotation(Command::class.java) ?: command.clazz.getAnnotation(Command::class.java) ?: return
        eventBundle.silent = command.annotation.defaultSilent
        if (data.devOnly && !isDev) {
            val eb = FancyEmbed()
                .addField("Woah there, slow down", "This command is only for Snuggles devs, and you are no developer!", false)

            event.replyEmbeds(eb.build())
                .bar(BarStyle.ERROR)
                .setEphemeral(true)
                .queue()

            return
        }

        val parameters = execution.parameters.drop(1).map { it.kotlinParameter(execution) }.toList()
        val args = fetchArguments(event, parameters).toTypedArray()

        Database.botStats.totalExecutions++

        execution.invoke(command.instance, eventBundle, *args)
    }

    private fun buildOptions(command: Method, verb: String): List<OptionData> {
        val options = mutableListOf<OptionData>()

        val parameters = command.parameters.drop(1)
        for (parameter in parameters) {
            options.add(parameter.toOption(command, verb))
        }

        return options
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun generateDescription(parameter: KParameter, verb: String): String {
        return when (parameter.type.javaType) {
            String::class.java -> "The {action} to {verb}"
            Int::class.java -> "The whole number to {verb}"
            Boolean::class.java -> "True/False"
            Float::class.java, Double::class.java -> "The number to {verb}"
            User::class.java -> "The user to {verb}"
            Member::class.java -> "The guild user to {verb}"
            GuildChannel::class.java -> "The channel to {verb}"
            else -> "The {action} to {verb}"
        }
            .replace("{verb}", verb)
            .replace("{action}", parameter.name ?: "action")
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun fetchArguments(event: SlashCommandEvent, types: List<KParameter>): List<Any> {
        val args = mutableListOf<Any>()

        for (option in event.options) {
            val parameter = types.find { it.name == option.name } ?: continue

            args.add(when (parameter.type.javaType) {
                String::class.java -> option.asString
                Int::class.java -> option.asLong.toInt()
                Boolean::class.java -> option.asBoolean
                Float::class.java -> option.asDouble.toFloat()
                Double::class.java -> option.asDouble
                User::class.java -> option.asUser
                Member::class.java -> option.asMember!!
                GuildChannel::class.java -> option.asGuildChannel
                else -> option.asString
            })
        }

        return args
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun Parameter.toOption(method: Method, verb: String): OptionData {
        val data: Description? = getAnnotation(Description::class.java)
        val description = data?.description ?: generateDescription(this.kotlinParameter(method), verb)
        val kParameter = this.kotlinParameter(method)

        return OptionData(
            when (kParameter.type.javaType) {
                String::class.java -> OptionType.STRING
                Int::class.java -> OptionType.INTEGER
                Boolean::class.java -> OptionType.BOOLEAN
                Float::class.java -> OptionType.NUMBER
                Double::class.java -> OptionType.NUMBER
                User::class.java, Member::class.java -> OptionType.USER
                GuildChannel::class.java -> OptionType.CHANNEL
                else -> OptionType.STRING
            },
            kParameter.name!!,
            description,
            !kParameter.isOptional
        )
    }

    private fun Command.getName(def: String): String {
        return if (name == "_reflection") def
            .lowercase()
            .replace("command", "")
        else name.lowercase()
    }
}