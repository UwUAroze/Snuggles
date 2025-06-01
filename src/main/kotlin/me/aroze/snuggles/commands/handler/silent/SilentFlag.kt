package me.aroze.snuggles.commands.handler.silent

/**
 * Annotation to mark an interaction as having a silent flag.
 * If the flag is set to true, the reply will be ephemeral (only visible to the user).
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class SilentFlag(
    /**
     * The default value for the silent flag.
     */
    val value: Boolean = true
)
