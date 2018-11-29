package com.benhero.glstudio.filter

import android.content.Context
import android.opengl.GLES20

/**
 * 发光滤镜
 *
 * @author Benhero
 * @date   2018/11/28
 */
class LightUpFilter(context: Context) : BaseFilter(context, VERTEX_SHADER, INVERSE_FRAGMENT_SHADER) {
    companion object {
        val INVERSE_FRAGMENT_SHADER = """
                precision mediump float;
                varying vec2 v_TexCoord;
                uniform sampler2D u_TextureUnit;
                uniform float uTime;
                void main() {
                    float lightUpValue = abs(sin(uTime / 1000.0)) / 4.0;
                    vec4 src = texture2D(u_TextureUnit, v_TexCoord);
                    vec4 addColor = vec4(lightUpValue, lightUpValue, lightUpValue, 1.0);
                    gl_FragColor = src + addColor;
                }
                """
    }

    private var uTime: Int = 0
    private var startTime: Long = 0

    override public fun onCreated() {
        super.onCreated()
        startTime = System.currentTimeMillis()
        uTime = getUniform("uTime")
    }

    override public fun onDraw() {
        super.onDraw()
        GLES20.glUniform1f(uTime, (System.currentTimeMillis() - startTime).toFloat())
    }
}