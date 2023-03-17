package me.aroze.snuggles.commands.handler

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Constraint(
    val min: Int = Int.MIN_VALUE,
    val max: Int = Int.MAX_VALUE,
)