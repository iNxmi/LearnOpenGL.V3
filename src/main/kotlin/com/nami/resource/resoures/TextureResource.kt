package com.nami.resource.resoures

import com.nami.constants.GamePaths
import com.nami.resource.Resource
import com.nami.texture.Texture
import com.nami.texture.Texture2D
import mu.KotlinLogging
import org.lwjgl.opengl.GL33.*
import java.nio.file.Path
import javax.imageio.ImageIO

class TextureResource : Resource<Texture2D>(GamePaths.textures, "png") {

    private val log = KotlinLogging.logger { }

    override fun onLoad(path: Path): Texture2D {
        val texture = Texture2D().bind()

            .parameter(Texture.Parameter.TEXTURE_MIN_FILTER, GL_NEAREST)
            .parameter(Texture.Parameter.TEXTURE_MAG_FILTER, GL_NEAREST)

            .parameter(Texture.Parameter.TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
            .parameter(Texture.Parameter.TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

            .image(ImageIO.read(path.toFile()), Texture.Format.RGB)

            .generateMipmap()

            .unbind()

        return texture
    }

}