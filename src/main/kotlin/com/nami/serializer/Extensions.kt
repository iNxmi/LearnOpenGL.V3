package com.nami.serializer

import kotlinx.serialization.KSerializer
import org.joml.*

fun ClosedFloatingPointRange<Float>.serializer(): KSerializer<ClosedFloatingPointRange<Float>> =
    SerializerClosedFloatingPointRangeFloat

fun IntRange.serializer(): KSerializer<IntRange> = SerializerIntRange

fun Matrix3f.serializer(): KSerializer<Matrix3f> = SerializerMatrix3f

fun Matrix4f.serializer(): KSerializer<Matrix4f> = SerializerMatrix4f

fun Quaternionf.serializer(): KSerializer<Quaternionf> = SerializerQuaternionf

fun Vector2f.serializer(): KSerializer<Vector2f> = SerializerVector2f
fun Vector2i.serializer(): KSerializer<Vector2i> = SerializerVector2i

fun Vector3f.serializer(): KSerializer<Vector3f> = SerializerVector3f
fun Vector3i.serializer(): KSerializer<Vector3i> = SerializerVector3i

fun Vector3f.serializerHexadecimal(): KSerializer<Vector3f> = SerializerVector3fHexadecimal
fun Vector3i.serializerHexadecimal(): KSerializer<Vector3i> = SerializerVector3iHexadecimal

fun Vector4f.serializerHexadecimal(): KSerializer<Vector4f> = SerializerVector4fHexadecimal
fun Vector4i.serializerHexadecimal(): KSerializer<Vector4i> = SerializerVector4iHexadecimal