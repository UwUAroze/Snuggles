package me.aroze.snuggles.commands.handler.silent

/**
 * Annotation to mark an interaction as having a silent flag. This adds a command argument for overriding whether the
 * command should respond ephemerally (silently).
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class SilentFlag(
    /**
     * The default value for the silent flag. If true, the command will respond ephemerally by default, otherwise it
     * will respond publicly.
     */
    val value: Boolean = true
)
