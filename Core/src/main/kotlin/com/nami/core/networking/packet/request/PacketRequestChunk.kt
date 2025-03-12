package com.nami.core.networking.packet.request

import com.nami.core.networking.packet.Packet

data class PacketRequestChunk(
    var id: Long
) : Packet {
    constructor() : this(0)
}