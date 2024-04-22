//package com.nami.register.registers
//
//import com.nami.constants.GamePaths
//import com.nami.mesh.Mesh
//import com.nami.register.Register
//import de.javagl.obj.ObjData
//import de.javagl.obj.ObjReader
//import de.javagl.obj.ObjUtils
//import mu.KotlinLogging
//import kotlin.io.path.reader
//
//class MeshRegister : Register<String, Mesh>(GamePaths.meshes) {
//
//    private val log = KotlinLogging.logger { }
//
//    override fun load(key: String): Mesh {
//        val path = path.resolve("$key.obj")
//        log.info { "Loading Mesh '$path'" }
//
//        var obj = ObjUtils.convertToRenderable(ObjReader.read(path.reader()))
//        obj = ObjUtils.triangulate(obj)
//
//        val positions = ObjData.getVerticesArray(obj)
//        val normals = ObjData.getNormalsArray(obj)
//        val uvs = ObjData.getTexCoordsArray(obj, 2)
//        val indices = ObjData.getFaceVertexIndicesArray(obj)
//
//        return Mesh(positions, null, normals, uvs, indices)
//    }
//
//}