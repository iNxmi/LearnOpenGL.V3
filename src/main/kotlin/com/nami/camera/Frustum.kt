package com.nami.camera

import com.nami.constants.Directions
import org.joml.Vector3f
import kotlin.math.tan

data class Frustum(val camera: Camera) {

    val halfVSide = camera.far * tan(camera.fov * 0.5f)
    val halfHSide = halfVSide * camera.aspect
    val frontMulFar = Vector3f(camera.directionFront).mul(camera.far)

    val planeNear =
        Plane(Vector3f(camera.transform.position)
                .add(Vector3f(camera.directionFront).mul(camera.near)),
            Vector3f(camera.directionFront))

    val planeFar =
        Plane(Vector3f(camera.transform.position)
                .add(frontMulFar),
            Vector3f(camera.directionFront)
                .mul(-1f))

    val planeRight = Plane(
        Vector3f(camera.transform.position),
        Vector3f(frontMulFar).sub(Vector3f(camera.directionRight).mul(halfHSide)).cross(Directions.UP)
    )

    val planeLeft = Plane(
        Vector3f(camera.transform.position),
        Vector3f(Directions.UP)
            .cross(Vector3f(frontMulFar).sub(Vector3f(camera.directionRight).mul(halfHSide)))
    )

    val planeTop = Plane(
        Vector3f(camera.transform.position),
        Vector3f(camera.directionRight)
            .cross(Vector3f(frontMulFar).sub(Vector3f(Directions.UP).mul(halfVSide)))
    )

    val planeBottom = Plane(
        Vector3f(camera.transform.position),
        Vector3f(frontMulFar)
            .sub(Vector3f(Directions.UP).mul(halfVSide)).cross(camera.directionRight)
    )

}