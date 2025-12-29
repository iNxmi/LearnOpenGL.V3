package com.nami.extension

import org.joml.*


fun Vector2f.toFloatArray() = floatArrayOf(this.x, this.y)
fun Vector2i.toIntArray() = intArrayOf(this.x, this.y)
fun Vector3f.toFloatArray() = floatArrayOf(this.x, this.y, this.z)
fun Vector3i.toIntArray() = intArrayOf(this.x, this.y, this.z)

//Addition
operator fun Vector2f.plus(other: Vector2f): Vector2f = Vector2f(this).add(other)
operator fun Vector3f.plus(other: Vector3f): Vector3f = Vector3f(this).add(other)
operator fun Vector4f.plus(other: Vector4f): Vector4f = Vector4f(this).add(other)

operator fun Vector2i.plus(other: Vector2i): Vector2i = Vector2i(this).add(other)
operator fun Vector3i.plus(other: Vector3i): Vector3i = Vector3i(this).add(other)
operator fun Vector4i.plus(other: Vector4i): Vector4i = Vector4i(this).add(other)

operator fun Vector2d.plus(other: Vector2d): Vector2d = Vector2d(this).add(other)
operator fun Vector3d.plus(other: Vector3d): Vector3d = Vector3d(this).add(other)
operator fun Vector4d.plus(other: Vector4d): Vector4d = Vector4d(this).add(other)

//Subtraction
operator fun Vector2f.minus(other: Vector2f): Vector2f = Vector2f(this).sub(other)
operator fun Vector3f.minus(other: Vector3f): Vector3f = Vector3f(this).sub(other)
operator fun Vector4f.minus(other: Vector4f): Vector4f = Vector4f(this).sub(other)

operator fun Vector2i.minus(other: Vector2i): Vector2i = Vector2i(this).sub(other)
operator fun Vector3i.minus(other: Vector3i): Vector3i = Vector3i(this).sub(other)
operator fun Vector4i.minus(other: Vector4i): Vector4i = Vector4i(this).sub(other)

operator fun Vector2d.minus(other: Vector2d): Vector2d = Vector2d(this).sub(other)
operator fun Vector3d.minus(other: Vector3d): Vector3d = Vector3d(this).sub(other)
operator fun Vector4d.minus(other: Vector4d): Vector4d = Vector4d(this).sub(other)

// Multiplication
operator fun Vector2f.times(other: Vector2f): Vector2f = Vector2f(this).mul(other)
operator fun Vector3f.times(other: Vector3f): Vector3f = Vector3f(this).mul(other)
operator fun Vector4f.times(other: Vector4f): Vector4f = Vector4f(this).mul(other)

operator fun Vector2i.times(other: Vector2i): Vector2i = Vector2i(this).mul(other)
operator fun Vector3i.times(other: Vector3i): Vector3i = Vector3i(this).mul(other)
operator fun Vector4i.times(other: Vector4i): Vector4i = Vector4i(this).mul(other)

operator fun Vector2d.times(other: Vector2d): Vector2d = Vector2d(this).mul(other)
operator fun Vector3d.times(other: Vector3d): Vector3d = Vector3d(this).mul(other)
operator fun Vector4d.times(other: Vector4d): Vector4d = Vector4d(this).mul(other)

// Scaling
operator fun Vector2f.times(scalar: Float): Vector2f = Vector2f(this).mul(scalar)
operator fun Vector3f.times(scalar: Float): Vector3f = Vector3f(this).mul(scalar)
operator fun Vector4f.times(scalar: Float): Vector4f = Vector4f(this).mul(scalar)

operator fun Vector2i.times(scalar: Int): Vector2i = Vector2i(this).mul(scalar)
operator fun Vector3i.times(scalar: Int): Vector3i = Vector3i(this).mul(scalar)
operator fun Vector4i.times(scalar: Int): Vector4i = Vector4i(this).mul(scalar)

operator fun Vector2d.times(scalar: Double): Vector2d = Vector2d(this).mul(scalar)
operator fun Vector3d.times(scalar: Double): Vector3d = Vector3d(this).mul(scalar)
operator fun Vector4d.times(scalar: Double): Vector4d = Vector4d(this).mul(scalar)

// Division
operator fun Vector2f.div(other: Vector2f): Vector2f = Vector2f(this).div(other)
operator fun Vector3f.div(other: Vector3f): Vector3f = Vector3f(this).div(other)
operator fun Vector4f.div(other: Vector4f): Vector4f = Vector4f(this).div(other)

operator fun Vector2i.div(other: Vector2i): Vector2i = Vector2i(this).div(other)
operator fun Vector3i.div(other: Vector3i): Vector3i = Vector3i(this).div(other)
operator fun Vector4i.div(other: Vector4i): Vector4i = Vector4i(this).div(other)

operator fun Vector2d.div(other: Vector2d): Vector2d = Vector2d(this).div(other)
operator fun Vector3d.div(other: Vector3d): Vector3d = Vector3d(this).div(other)
operator fun Vector4d.div(other: Vector4d): Vector4d = Vector4d(this).div(other)

// Get
operator fun Vector2f.get(index: Int): Float = this.get(index)
operator fun Vector3f.get(index: Int): Float = this.get(index)
operator fun Vector4f.get(index: Int): Float = this.get(index)

operator fun Vector2i.get(index: Int): Int = this.get(index)
operator fun Vector3i.get(index: Int): Int = this.get(index)
operator fun Vector4i.get(index: Int): Int = this.get(index)

operator fun Vector2d.get(index: Int): Double = this.get(index)
operator fun Vector3d.get(index: Int): Double = this.get(index)
operator fun Vector4d.get(index: Int): Double = this.get(index)