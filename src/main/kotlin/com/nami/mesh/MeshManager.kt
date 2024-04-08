package com.nami.mesh

import com.nami.constants.GamePaths
import com.nami.texture.Texture
import com.nami.texture.TextureType
import de.javagl.obj.ObjData
import de.javagl.obj.ObjReader
import de.javagl.obj.ObjUtils
import mu.KotlinLogging
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.AINode
import org.lwjgl.assimp.AIScene
import org.lwjgl.assimp.Assimp
import kotlin.io.path.pathString
import kotlin.io.path.reader

class MeshManager {

    companion object {

        private val log = KotlinLogging.logger { }
        private val meshes = mutableMapOf<String, Mesh>()

        @JvmStatic
        fun get(name: String): Mesh {
            if (!meshes.containsKey(name))
                meshes[name] = load(name)

            return meshes[name]!!
        }

        private fun load(name: String): Mesh {
            val path = GamePaths.meshes.resolve("$name.obj")
            log.info { "Loading Mesh '$path'" }

            var obj = ObjUtils.convertToRenderable(ObjReader.read(path.reader()))
            obj = ObjUtils.triangulate(obj)

            val positions = ObjData.getVerticesArray(obj)
            val normals = ObjData.getNormalsArray(obj)
            val uvs = ObjData.getTexCoordsArray(obj, 2)
            val indices = ObjData.getFaceVertexIndicesArray(obj)

            return Mesh(positions, normals, uvs, indices)
        }

    }

}