package com.nami.resources.texture

import com.nami.resources.GamePath
import com.nami.resources.Resources
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import java.nio.file.Path
import javax.imageio.ImageIO

class ResourceLoaderTexture : Resources<Texture>(GamePath.texture, "texture", arrayOf("png", "jpg", "jpeg")) {

    override fun onLoad(id: String, path: Path): Texture {
        return Texture(id, ImageIO.read(path.toUri().toURL()))
    }

    override fun onLoadCompleted() {
        TextureAtlas.generate()
    }

    fun unbind() {
        GL11.glBindTexture(GL_TEXTURE_2D, 0)
    }

}