package com.nami.register

import com.nami.register.registers.*

abstract class Register<T, U> {

    companion object {
        @JvmStatic
        val texture = TextureRegister()

        @JvmStatic
        val model = ModelRegister()

        @JvmStatic
        val mesh = MeshRegister()

        @JvmStatic
        val shader = ShaderRegister()

        @JvmStatic
        val sound = SoundRegister()
    }

    private val register = mutableMapOf<T, U>()

    protected abstract fun load(key: T): U

    fun put(key: T, value: U) : Register<T, U> {
        if(register.containsKey(key))
            throw RuntimeException("Key '$key' is already occupied.")

        register[key] = value
        return this
    }

    fun get(key: T): U {
        if (!register.containsKey(key))
            register[key] = load(key)

        return register[key]!!
    }

}