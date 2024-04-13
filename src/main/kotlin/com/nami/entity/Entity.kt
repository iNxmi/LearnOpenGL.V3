package com.nami.entity

import com.nami.model.Model
import com.nami.shader.Shader

open class Entity(private var model: Model? = null) {

    val transform = Transform()

    fun render(shader: Shader) {
        if(model == null)
            return

        shader.uniform
            .set("u_model_matrix", transform.modelMatrix())
            .set("u_normal_matrix", transform.normalMatrix())

        model!!.render(shader)
    }

}