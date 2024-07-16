package com.nami.resources.model

import com.nami.resources.GamePath
import com.nami.resources.Resources
import de.javagl.obj.ObjData
import de.javagl.obj.ObjReader
import de.javagl.obj.ObjUtils
import java.nio.file.Files
import java.nio.file.Path

class ResourceLoaderModel : Resources<Model>(GamePath.model, "model", arrayOf("obj")) {
    override fun onLoad(id: String, path: Path): Model {
        val obj = ObjUtils.convertToRenderable(ObjReader.read(Files.newInputStream(path)))

        return Model(
            id,
            ObjData.getVertices(obj),
            ObjData.getTexCoords(obj, 2),
            ObjData.getNormals(obj),
            ObjData.getFaceVertexIndices(obj)
        )
    }

    override fun onLoadCompleted() {

    }

}