package com.benhero.glstudio.filter

import android.content.Context
import android.opengl.GLES20

/**
 * 发光滤镜
 *
 * @author Benhero
 * @date   2018/11/28
 */
class LightUpFilter(context: Context) : BaseFilter(context, VERTEX_SHADER, FRAGMENT_SHADER) {
    companion object {
        const val FRAGMENT_SHADER = """
                precision mediump float;
                varying vec2 v_TexCoord;
                uniform sampler2D u_TextureUnit;
                uniform float intensity;
                void main() {
                    vec4 src = texture2D(u_TextureUnit, v_TexCoord);
                    vec4 addColor = vec4(intensity, intensity, intensity, 1.0);
                    gl_FragColor = src + addColor;
                }
                """
    }

    private var intensityLocation: Int = 0
    private var startTime: Long = 0

    override fun onCreated() {
        super.onCreated()
        startTime = System.currentTimeMillis()
        intensityLocation = getUniform("intensity")
    }

    override fun onDraw() {
        super.onDraw()
        val intensity = Math.abs(Math.sin((System.currentTimeMillis() - startTime) / 1000.0)) / 4.0
        GLES20.glUniform1f(intensityLocation, intensity.toFloat())
    }
}