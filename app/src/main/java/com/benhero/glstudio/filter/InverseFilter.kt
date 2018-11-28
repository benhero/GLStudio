package com.benhero.glstudio.filter

import android.content.Context

/**
 * 反色滤镜
 *
 * @author Benhero
 * @date   2018/11/28
 */
class InverseFilter(context: Context) : BaseFilter(context, VERTEX_SHADER, INVERSE_FRAGMENT_SHADER) {
    companion object {
        val INVERSE_FRAGMENT_SHADER = """
                precision mediump float;
                varying vec2 v_TexCoord;
                uniform sampler2D u_TextureUnit;
                void main() {
                    vec4 src = texture2D(u_TextureUnit, v_TexCoord);
                    gl_FragColor =vec4(1.0 - src.r, 1.0 - src.g, 1.0 - src.b, 1.0);
                }
                """
    }
}