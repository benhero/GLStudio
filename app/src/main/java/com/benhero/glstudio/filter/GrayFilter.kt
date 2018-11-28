package com.benhero.glstudio.filter

import android.content.Context

/**
 * 灰色滤镜
 *
 * @author Benhero
 * @date   2018/11/28
 */
class GrayFilter(context: Context) : BaseFilter(context, VERTEX_SHADER, GRAY_FRAGMENT_SHADER) {
    companion object {
        val GRAY_FRAGMENT_SHADER = """
                precision mediump float;
                varying vec2 v_TexCoord;
                uniform sampler2D u_TextureUnit;
                void main() {
                    vec4 src = texture2D(u_TextureUnit, v_TexCoord);
                    float gray = (src.r + src.g + src.b) / 3.0;
                    gl_FragColor =vec4(gray, gray, gray, 1.0);
                }
                """
    }
}