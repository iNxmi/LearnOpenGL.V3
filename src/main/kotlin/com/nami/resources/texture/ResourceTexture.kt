package com.nami.resources.texture

import com.nami.resources.GamePath
import com.nami.resources.Resource
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import java.nio.file.Path
import javax.imageio.ImageIO

class ResourceTexture : Resource<Texture>(GamePath.textures, arrayOf("png", "jpg", "jpeg")) {

    override fun onLoad(path: Path): Texture {
        return Texture(ImageIO.read(path.toUri().toURL()))
    }

    override fun onLoadCompleted() {
        TextureAtlas.generate()
    }

    fun unbind() {
        GL11.glBindTexture(GL_TEXTURE_2D, 0)
    }

}