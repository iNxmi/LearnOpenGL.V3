package com.nami.world

import org.joml.Vector3f

class BlockTopAndSideColor(colorTop: Vector3f = Vector3f(), colorSide: Vector3f = Vector3f()) : Block(
    colorTop = Vector3f(colorTop),
    colorBack = Vector3f(colorSide),
    colorLeft = Vector3f(colorSide),
    colorFront = Vector3f(colorSide),
    colorRight = Vector3f(colorSide),
    colorBottom = Vector3f(colorSide)
)