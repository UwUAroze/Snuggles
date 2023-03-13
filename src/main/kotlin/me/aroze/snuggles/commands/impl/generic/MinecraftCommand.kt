package me.aroze.snuggles.commands.impl.generic

import kong.unirest.Unirest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import me.aroze.snuggles.commands.handler.Description
import me.aroze.snuggles.utils.BarStyle
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar
import net.dv8tion.jda.api.utils.FileUpload
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

@Command(
    description = "Look up some info on a Minecraft player or server",
)
class MinecraftCommand {

    @Command(description = "Looks up info on a Minecraft player")
    fun profile(event: CommandEvent, @Description("The username or uuid of the player") player: String) = runBlocking {

        launch {
            val eb = FancyEmbed()

            val profile = Unirest.get("https://api.ashcon.app/mojang/v2/user/$player").asJson()
            val error = profile.status != 200

            if (error) {
                eb.setTitle("You messed up!")
                eb.setDescription("What in the fuck is `$player`\nThat's not a Minecraft username, nor a UUID, you moron.")
                event.message.replyEmbeds(eb.build())
                    .bar(BarStyle.ERROR)
                    .setEphemeral(true)
                    .queue()
                return@launch
            }

            val body = profile.body.`object`
            val uuid = body.getString("uuid")
            val name = body.getString("username")

            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val hasDate = !body.isNull("created_at")
            val creationDate = if (!hasDate) null else sdf.parse(body.getString("created_at")).toInstant().epochSecond

            val skin = URL("https://skins.mcstats.com/body/front/$uuid").openStream()
            val textures = body.getJSONObject("textures")
            val skinLink = textures.getJSONObject("skin").getString("url")
            val capeLink = textures.optJSONObject("cape")?.getString("url")

            eb.setTitle(name)
            eb.setDescription(
                " > UUID: `$uuid`\n" +
                    " > Creation date: ${if (creationDate == null) "`No idea, sorry! Jk, fuck you.`" else "<t:$creationDate:d>, which was <t:$creationDate:R>"}\n" +
                    "\n" +
                    " > NameMC: [Click Here](https://namemc.com/profile/$name)\n" +
                    " > Skin: [Download Here]($skinLink)\n" +
                    " > Cape: ${if (capeLink == null) "No vanilla cape equipped" else "[Download Here]($capeLink)"}\n\n"
            )

            eb.setThumbnail("attachment://skin.png")

            event.message.replyEmbeds(eb.build())
                .addFiles(FileUpload.fromData(skin, "skin.png"))
                .bar(BarStyle.PINK)
                .setEphemeral(event.silent)
                .queue {
                    skin.close()
                }
        }
    }

}