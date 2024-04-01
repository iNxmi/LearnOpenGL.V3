package com.nami.nuklear

import org.lwjgl.nuklear.NkAllocator
import org.lwjgl.nuklear.NkContext
import org.lwjgl.nuklear.NkUserFont
import org.lwjgl.nuklear.Nuklear

class NKManager {

    companion object {

        @JvmStatic
        val ctx: NkContext = NkContext.create()
        @JvmStatic
        val font: NkUserFont = NkUserFont.create()

        @JvmStatic
        fun init() {
            Nuklear.nk_init(ctx, NkAllocator.create(),null)
        }

    }

}