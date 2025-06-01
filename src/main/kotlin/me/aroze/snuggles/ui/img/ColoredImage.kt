package me.aroze.snuggles.ui.img

import me.aroze.snuggles.constants.BarColor
import net.dv8tion.jda.api.components.mediagallery.MediaGallery
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem
import net.dv8tion.jda.api.utils.FileUpload
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class ColoredImage(
    private val color: Color = Color(0, 0, 0),
    private val width: Int = 100,
    private val height: Int = 100
) {

    fun create(): BufferedImage {
        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics = bufferedImage.createGraphics()
        graphics.paint = color
        graphics.fillRect(0, 0, width, height)
        return bufferedImage
    }

}

fun coloredBar(
    color: Color = Color(0, 0, 0),
    width: Int = 100,
    height: Int = 5
): MediaGallery {
    val image = ColoredImage(Color(0xffd4ff), 230, 5).create()
    val outputStream = ByteArrayOutputStream()
    ImageIO.write(image, "png", outputStream)

    val item = MediaGalleryItem.fromFile(FileUpload.fromData(outputStream.toByteArray(), "stupid_bar.png"))
    return MediaGallery.of(item)
}

fun coloredBar(
    color: BarColor,
    width: Int = 100,
    height: Int = 5
) = coloredBar(color.color, width, height)
