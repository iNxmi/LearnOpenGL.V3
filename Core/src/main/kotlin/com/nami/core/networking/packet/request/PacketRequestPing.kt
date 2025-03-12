package com.nami.core.networking.packet.request

import com.nami.core.networking.packet.Packet

data class PacketRequestPing(
    var timeMsRequest: Long = 0
) : Packet {
    constructor() : this(0)
}
