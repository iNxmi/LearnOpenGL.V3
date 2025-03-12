package com.nami.core.networking.packet.response

import com.nami.core.Block
import com.nami.core.networking.packet.Packet

data class PacketResponseChunk(
    var blocks: Map<Long, Block>
) : Packet {
    constructor() : this(mapOf())
}