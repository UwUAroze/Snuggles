package me.aroze.snuggles.commands.handler

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Description(
    val description: String
)
