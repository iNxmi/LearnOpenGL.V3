package com.nami.camera

import com.nami.entity.Transform

interface BoundingVolume {

    fun isOnFrustum(frustum: Frustum, transform: Transform): Boolean

}