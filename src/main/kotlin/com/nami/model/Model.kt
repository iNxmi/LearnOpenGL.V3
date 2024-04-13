package com.nami.model

import com.nami.mesh.Mesh
import com.nami.register.Register
import com.nami.shader.Shader
import com.nami.texture.Texture
import mu.KotlinLogging

class Model(private val mesh: Mesh, private val material: Material) {

    private val log = KotlinLogging.logger { }

    fun render(shader: Shader) {
        shader.uniform.set("u_material.color", material.color)

        setTexture(Texture.Type.DIFFUSE, shader, 0)
        setTexture(Texture.Type.SPECULAR, shader, 1)

        shader.uniform.set("u_material.specular_exponent", material.specularExponent)

        mesh.render()

        Register.texture.unbind()
    }

    private fun setTexture(type: Texture.Type, shader: Shader, slot: Int) {
        val hasTexture = material.textures.containsKey(type)

        val texture = if (hasTexture)
            material.textures[type]!!
        else
            Register.texture.get(Pair("_fallback", type))

        texture.bind(slot)
        shader.uniform.set("u_material.use_texture_${type.id}", hasTexture)
        shader.uniform.set("u_material.texture_${type.id}", slot)
    }

}