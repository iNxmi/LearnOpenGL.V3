package com.nami.core.networking.packet.response

import com.nami.core.networking.packet.Packet

data class PacketResponsePing(
    var timeMsRequest: Long = 0,
    var timeMsResponse: Long = 0
) : Packet {
    constructor() : this(0, 0)
}