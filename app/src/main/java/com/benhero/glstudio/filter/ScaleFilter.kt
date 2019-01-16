package com.benhero.glstudio.filter

import android.content.Context
import android.opengl.GLES20

/**
 * 缩放滤镜
 *
 * @author Benhero
 * @date   2018/11/28
 */
class ScaleFilter(context: Context) : BaseFilter(context, VERTEX_SHADER, INVERSE_FRAGMENT_SHADER) {
    companion object {
        val INVERSE_FRAGMENT_SHADER = """
                precision mediump float;
                varying vec2 v_TexCoord;
                uniform sampler2D u_TextureUnit;
                uniform float uTime;

                vec2 scale(vec2 srcCoord, float xScale, float yScale) {
                    return vec2((srcCoord.x - 0.5) / xScale + 0.5, (srcCoord.y - 0.5) / yScale + 0.5);
                }

                void main() {
                    float scaleValue = abs(sin(uTime / 1000.0)) + 0.5;
                    vec2 offsetTexCoord = scale(v_TexCoord, scaleValue, scaleValue);
                    if (offsetTexCoord.x >= 0.0 && offsetTexCoord.x <= 1.0 &&
                        offsetTexCoord.y >= 0.0 && offsetTexCoord.y <= 1.0) {
                        gl_FragColor = texture2D(u_TextureUnit, offsetTexCoord);
                    }
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