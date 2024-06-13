package com.nami.camera

import com.nami.entity.Transform
import org.joml.Vector3f

class SphereBoundingVolume(val position: Vector3f, val radius: Float) : BoundingVolume {

    override fun isOnFrustum(frustum: Frustum, transform: Transform): Boolean {
//        val modelMatrix = transform.modelMatrix()
//        val globalScale = Vector3f(modelMatrix.getScale(null))
//
//        val globalCenter = Vector4f(position, 1.0f).mul(modelMatrix)
////        modelMatrix.
//
//        val maxScale = max(max(globalScale.x, globalScale.y), globalScale.z)
//
//        val globalSphere = SphereBoundingVolume(globalCenter, radius * maxScale * 0.5f)
//
//        return (globalSphere.isOnOrForwardPlane(frustum.planeLeft) &&
//                globalSphere.isOnOrForwardPlane(frustum.planeRight) &&
//                globalSphere.isOnOrForwardPlane(frustum.planeFar) &&
//                globalSphere.isOnOrForwardPlane(frustum.planeNear) &&
//                globalSphere.isOnOrForwardPlane(frustum.planeTop) &&
//                globalSphere.isOnOrForwardPlane(frustum.planeBottom))
        TODO()
    }


    fun isOnOrForwardPlane(plane: Plane): Boolean {
        return plane.getSignedDistanceToPlane(position) > -radius
    }

}