package com.benhero.glstudio.renderer

import android.content.Context
import android.opengl.GLES20
import com.benhero.glstudio.R
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.base.GLConstants.POSITION_COMPONENT_COUNT
import com.benhero.glstudio.base.GLConstants.TEX_VERTEX_COMPONENT_COUNT
import com.benhero.glstudio.util.BufferUtil
import com.benhero.glstudio.util.ProjectionMatrixHelper
import com.benhero.glstudio.util.TextureHelper
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 蒙版遮罩
 *
 * @author Benhero
 * @date   2018/10/30
 */
class L6_3_TextureRenderer(context: Context) : BaseRenderer(context) {
    companion object {
        val VERTEX_SHADER = """
            uniform mat4 u_Matrix;
            attribute vec4 a_Position;
            attribute vec2 a_TexCoord;
            varying vec2 v_TexCoord;
            void main() {
                v_TexCoord = a_TexCoord;
                gl_Position = u_Matrix * a_Position;
            }
            """
        val FRAGMENT_SHADER = """
            precision mediump float;
            varying vec2 v_TexCoord;
            uniform sampler2D u_TextureUnit1;
            uniform sampler2D u_TextureUnit2;
            uniform sampler2D u_TextureUnit3;
            void main() {
                vec4 texture1 = texture2D(u_TextureUnit1, v_TexCoord);
                vec4 texture2 = texture2D(u_TextureUnit2, v_TexCoord);
                vec4 texture3 = texture2D(u_TextureUnit3, v_TexCoord);
                if (v_TexCoord.x < 0.0 || v_TexCoord.x > 1.0 || v_TexCoord.y < 0.0 || v_TexCoord.y > 1.0) {
                    gl_FragColor = vec4(0.0);
                    return;
                }
                if (texture3.r == 0.0) {
                    gl_FragColor = texture2;
                } else {
                    gl_FragColor = texture1;
                }
            }
            """
        private val PIKACHU_VERTEX_DATA = floatArrayOf(-1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f)

        private val TEXTURE_DATA = floatArrayOf(0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f)
    }

    private val pikachuVertexBuffer: FloatBuffer
    private val textureBuffer: FloatBuffer
    lateinit var projectionMatrixHelper: ProjectionMatrixHelper
    private var textureLocation1: Int = 0
    private var textureLocation2: Int = 0
    private var textureLocation3: Int = 0
    private var positionLocation: Int = 0
    private var pikachuBean: TextureHelper.TextureBean? = null
    private var maskBean: TextureHelper.TextureBean? = null
    private var tuzkiBean: TextureHelper.TextureBean? = null

    init {
        pikachuVertexBuffer = BufferUtil.createFloatBuffer(PIKACHU_VERTEX_DATA)
        textureBuffer = BufferUtil.createFloatBuffer(TEXTURE_DATA)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(1f, 0f, 0f, 1f)
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER)
        projectionMatrixHelper = ProjectionMatrixHelper(program, "u_Matrix")

        positionLocation = getAttrib("a_Position")

        val texCoordLocation = getAttrib("a_TexCoord")
        textureLocation1 = getUniform("u_TextureUnit1")
        textureLocation2 = getUniform("u_TextureUnit2")
        textureLocation3 = getUniform("u_TextureUnit3")

        pikachuBean = TextureHelper.loadTexture(context, R.drawable.pikachu)
        maskBean = TextureHelper.loadTexture(context, R.drawable.square)
        tuzkiBean = TextureHelper.loadTexture(context, R.drawable.tuzki)

        textureBuffer.position(0)
        GLES20.glVertexAttribPointer(texCoordLocation, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, textureBuffer)
        GLES20.glEnableVertexAttribArray(texCoordLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        GLES20.glViewport(0, 0, width, height)
        projectionMatrixHelper.enable(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT)
        // 开启纹理透明混合，这样才能绘制透明图片
        GLES20.glEnable(GL10.GL_BLEND)
        GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)

        drawPikachu()
        drawMask()
        drawTuzki()


        GLES20.glEnableVertexAttribArray(positionLocation)
        pikachuVertexBuffer.position(0)
        GLES20.glVertexAttribPointer(positionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, pikachuVertexBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, PIKACHU_VERTEX_DATA.size / POSITION_COMPONENT_COUNT)
    }

    private fun drawPikachu() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, pikachuBean!!.textureId)
        GLES20.glUniform1i(textureLocation1, 0)
    }

    private fun drawTuzki() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tuzkiBean!!.textureId)
        GLES20.glUniform1i(textureLocation2, 1)
    }

    private fun drawMask() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, maskBean!!.textureId)
        GLES20.glUniform1i(textureLocation3, 2)
    }
}