package com.nami.register.registers

import com.nami.constants.GamePaths
import com.nami.model.Material
import com.nami.model.Model
import com.nami.register.Register
import com.nami.texture.Texture
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.joml.Vector3f
import java.awt.Color
import java.nio.file.Files

class ModelRegister : Register<String, Model>() {

    private val log = KotlinLogging.logger { }

    override fun load(key: String): Model {
        val path = GamePaths.models.resolve("$key.json")
        log.info { "Loading Model '$path'" }

        val template = Json.decodeFromString<ModelTemplate>(Files.readString(path))

        val mesh = mesh.get(template.mesh)
        val material = loadMaterial(template)

        return Model(mesh, material)
    }

    private fun loadMaterial(template: ModelTemplate): Material {
        val color = Vector3f(1f)
        val textures = mutableMapOf<Texture.Type, Texture>()

        if (template.material.color != null) {
            val c = Color.decode(template.material.color)
            color.set(c.red / 255f, c.green / 255f, c.blue / 255f);
        }

        val texturesTemplate = template.material.textures
        if (texturesTemplate != null) {
            for (t in texturesTemplate.types) {
                val type = Texture.Type.from(t)
                textures[type] = texture.get(Pair(texturesTemplate.name, type))
            }
        }

        val material = Material(
            color,
            textures.toMap(),
            template.material.specularExponent
        )

        return material
    }

    @Serializable
    data class TexturesTemplate(
        val name: String,
        val types: List<String>,
        val filter: String
    )

    @Serializable
    data class MaterialTemplate(
        var textures: TexturesTemplate? = null,
        val specularExponent: Float,
        var color: String? = null
    )

    @Serializable
    data class ModelTemplate(
        val mesh: String,
        val material: MaterialTemplate
    )

}