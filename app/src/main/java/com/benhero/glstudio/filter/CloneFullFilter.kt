package com.benhero.glstudio.filter

import android.content.Context
import android.opengl.GLES20

/**
 * 完全分身克隆滤镜
 *
 * @author Benhero
 * @date   2019/1/18
 */
class CloneFullFilter(context: Context) : BaseFilter(context, VERTEX_SHADER, FRAGMENT_SHADER) {
    companion object {
        const val FRAGMENT_SHADER = """
            precision mediump float;
            varying vec2 v_TexCoord;
            uniform sampler2D u_TextureUnit;
            uniform float cloneCount;
            void main() {
                gl_FragColor = texture2D(u_TextureUnit, v_TexCoord * cloneCount);
            }
        """
    }

    override fun onCreated() {
        super.onCreated()
        GLES20.glUniform1f(getUniform("cloneCount"), 3.0f)
    }
}