package com.nami.model

import com.nami.constants.GamePaths
import com.nami.mesh.MeshManager
import com.nami.texture.Texture
import com.nami.texture.TextureManager
import com.nami.texture.TextureType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.joml.Vector3f
import java.awt.Color
import java.nio.file.Files

class ModelManager {

    companion object {

        private val log = KotlinLogging.logger { }
        private val models = mutableMapOf<String, Model>()

        @JvmStatic
        fun get(name: String): Model {
            if (!models.containsKey(name))
                models[name] = loadModel(name)

            return models[name]!!
        }

        private fun loadModel(name: String): Model {
            val path = GamePaths.models.resolve("$name.json")
            log.info { "Loading Model '$path'" }

            val template = Json.decodeFromString<ModelTemplate>(Files.readString(path))

            val mesh = MeshManager.get(template.mesh)
            val material = loadMaterial(template)

            return Model(mesh, material)
        }

        private fun loadMaterial(template: ModelTemplate): Material {
            val color = Vector3f(1f)
            val textures = mutableMapOf<TextureType, Texture>()

            if (template.material.color != null) {
                val c = Color.decode(template.material.color)
                color.set(c.red / 255f, c.green / 255f, c.blue / 255f);
            }

            val texturesTemplate = template.material.textures
            if (texturesTemplate != null) {
                for (t in texturesTemplate.types) {
                    val type = TextureType.from(t)
                    val scaling = TextureManager.ScalingFunction.from(texturesTemplate.filter)

                    val texture = TextureManager.load(texturesTemplate.name, type, scaling)
                    textures[type] = texture
                }
            }

            val material = Material(
                color,
                textures.toMap(),
                template.material.specularExponent
            )

            return material
        }

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