package com.benhero.glstudio.filter

import android.content.Context
import android.opengl.GLES20

/**
 * 位移滤镜
 *
 * @author Benhero
 * @date   2019-1-17
 */
class TranslateFilter(context: Context) : BaseFilter(context, VERTEX_SHADER, INVERSE_FRAGMENT_SHADER) {
    companion object {
        val INVERSE_FRAGMENT_SHADER = """
                precision mediump float;
                varying vec2 v_TexCoord;
                uniform sampler2D u_TextureUnit;
                uniform float xV;
                uniform float yV;

                vec2 translate(vec2 srcCoord, float x, float y) {
                    return vec2(srcCoord.x + x, srcCoord.y + y);
                }

                void main() {
                    vec2 offsetTexCoord = translate(v_TexCoord, xV, yV);
                    if (offsetTexCoord.x >= 0.0 && offsetTexCoord.x <= 1.0 &&
                        offsetTexCoord.y >= 0.0 && offsetTexCoord.y <= 1.0) {
                        gl_FragColor = texture2D(u_TextureUnit, offsetTexCoord);
                    }
                }
                """
    }

    private var xLocation: Int = 0
    private var yLocation: Int = 0
    private var startTime: Long = 0

    override public fun onCreated() {
        super.onCreated()
        startTime = System.currentTimeMillis()
        xLocation = getUniform("xV")
        yLocation = getUniform("yV")
    }

    override public fun onDraw() {
        super.onDraw()
        val intensity = Math.sin((System.currentTimeMillis() - startTime) / 1000.0) * 0.5
        GLES20.glUniform1f(xLocation, intensity.toFloat())
        GLES20.glUniform1f(yLocation, 0.0f)
    }
}