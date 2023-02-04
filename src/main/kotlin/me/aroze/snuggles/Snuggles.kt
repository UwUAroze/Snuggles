import me.aroze.snuggles.initialisation.Login.login
import net.dv8tion.jda.api.JDA

lateinit var jda: JDA

fun main(args: Array<String>) {
    login()
    println(jda.selfUser.asTag)
}