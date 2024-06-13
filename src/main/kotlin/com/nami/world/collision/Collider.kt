package com.nami.world.collision

interface Collider {

    fun isColliding(sphere: SphereCollider): CollisionPoints
    fun isColliding(sphere: PlaneCollider): CollisionPoints
    fun isColliding(sphere: AABBCollider): CollisionPoints

}