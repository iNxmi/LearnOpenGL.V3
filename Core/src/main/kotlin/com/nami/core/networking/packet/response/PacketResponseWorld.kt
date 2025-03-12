package com.nami.core.networking.packet.response

import com.nami.core.networking.packet.Packet
import org.joml.Vector3i

data class PacketResponseWorld(
    val size: Vector3i,
    val seed: Long,
    val waterLevel: Int
) : Packet {
    constructor() : this(Vector3i(), 0, 0)
}