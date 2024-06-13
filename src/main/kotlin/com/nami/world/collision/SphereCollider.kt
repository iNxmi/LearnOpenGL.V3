package com.nami.world.collision

import org.joml.Vector3f

class SphereCollider(val position: Vector3f, val radius: Float) : Collider {

    override fun isColliding(sphere: SphereCollider): CollisionPoints {
        TODO()
    }

    override fun isColliding(sphere: PlaneCollider): CollisionPoints {
        TODO()
    }

    override fun isColliding(sphere: AABBCollider): CollisionPoints {
        TODO()
    }

}