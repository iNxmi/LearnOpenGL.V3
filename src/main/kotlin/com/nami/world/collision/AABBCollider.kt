package com.nami.world.collision

import org.joml.Vector3f

class AABBCollider(var position: Vector3f, var size: Vector3f) : Collider {

    fun intersects(aabb: AABBCollider) {
        TODO()
    }

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