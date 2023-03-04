package me.aroze.snuggles.commands.handler

import net.dv8tion.jda.api.Permission
import java.lang.reflect.Method
import kotlin.reflect.KVisibility
import kotlin.reflect.jvm.kotlinFunction

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Command(
    val name: String = "_reflection",
    val description: String = "No description provided",
    val devOnly: Boolean = false,
    val defaultSilent: Boolean = false,
    val global: Boolean = false,
    val silentToggle: Boolean = true,
    val permissions: Array<Permission> = [],
) {
    companion object {
        fun getApplicableMethods(clazz: Class<*>): List<Method> {
            return clazz.declaredMethods.filter {
                it.isAnnotationPresent(Command::class.java)
                    || it.kotlinFunction?.visibility == KVisibility.PUBLIC
            }
        }

        fun getMainCommand(clazz: Class<*>): Method? {
            return getApplicableMethods(clazz).find { it.name.lowercase() == "main" }
        }

        fun getSubCommands(clazz: Class<*>): List<Method> {
            val main = getMainCommand(clazz)
            return getApplicableMethods(clazz).filter { it != main }
        }

        fun getSubCommandGroups(clazz: Class<*>): List<Class<*>> {
            return clazz.declaredClasses.filter { it.kotlin.visibility == KVisibility.PUBLIC }
        }

    }
}