package com.nami.engine.camera

import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

interface Camera {

    val position: Vector3f
    val rotation: Quaternionf

    val projection: Matrix4f
    val view: Matrix4f

}