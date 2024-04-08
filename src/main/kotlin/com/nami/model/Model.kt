package com.nami.model

import com.nami.mesh.Mesh
import com.nami.shader.ShaderProgram
import com.nami.texture.TextureManager
import com.nami.texture.TextureType
import mu.KotlinLogging

class Model(private val mesh: Mesh, private val material: Material) {

    private val log = KotlinLogging.logger {  }
    fun render(shader: ShaderProgram) {
        shader.uniform.set("u_material.color", material.color)

        if(material.textures.containsKey(TextureType.DIFFUSE)) {
            material.textures[TextureType.DIFFUSE]!!.bind(0)
            shader.uniform.set("u_material.texture_diffuse", 0)
            shader.uniform.set("u_material.use_texture_diffuse", true)
        } else {
            TextureManager.bind("_fallback", TextureType.DIFFUSE, 0)
            shader.uniform.set("u_material.texture_diffuse", 0)
            shader.uniform.set("u_material.use_texture_diffuse", false)
        }

        if(material.textures.containsKey(TextureType.SPECULAR)) {
            material.textures[TextureType.SPECULAR]!!.bind(1)
            shader.uniform.set("u_material.texture_specular", 1)
            shader.uniform.set("u_material.use_texture_specular", true)
        } else {
            TextureManager.bind("_fallback", TextureType.SPECULAR, 1)
            shader.uniform.set("u_material.texture_specular", 1)
            shader.uniform.set("u_material.use_texture_specular", false)
        }

        shader.uniform.set("u_material.specular_exponent", material.specularExponent)

        mesh.render()

        TextureManager.unbind()
    }

}