package me.aroze.snuggles.commands.handler

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Autocomplete(
    val options: Array<String> = [],
    val force: Boolean = false
)