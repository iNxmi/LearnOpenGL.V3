package com.nami.entity

import com.nami.model.Model
import com.nami.shader.ShaderProgram

open class Entity(private var model: Model? = null) {

    val transform = Transform()

    fun render(shader: ShaderProgram) {
        if(model == null)
            return

        shader.uniform
            .set("u_model_matrix", transform.modelMatrix())
            .set("u_normal_matrix", transform.normalMatrix())

        model!!.render(shader)
    }

}