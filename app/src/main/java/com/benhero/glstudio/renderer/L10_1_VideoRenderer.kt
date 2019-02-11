package com.benhero.glstudio.renderer

import android.content.Context
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.view.Surface
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.util.BufferUtil
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * 视频绘制
 *
 * @author Benhero
 */
class L10_1_VideoRenderer(context: Context, private val mediaPlayer: MediaPlayer) : BaseRenderer(context), SurfaceTexture.OnFrameAvailableListener {
    companion object {
        private const val VERTEX_SHADER = """
                attribute vec4 a_Position;
                attribute vec2 a_TexCoord;
                varying vec2 v_TexCoord;
                void main() {
                    v_TexCoord = a_TexCoord;
                    gl_Position = a_Position;
                }
                """
        private const val FRAGMENT_SHADER = """
                #extension GL_OES_EGL_image_external : require
                precision mediump float;
                varying vec2 v_TexCoord;
                uniform samplerExternalOES u_TextureUnit;
                void main() {
                    gl_FragColor = texture2D(u_TextureUnit, v_TexCoord);
                }
                """

        private const val POSITION_COMPONENT_COUNT = 2
        private val SCALE = 9.0f / 16.0f

        private val POINT_DATA = floatArrayOf(
                -1.0f, -1.0f * SCALE * SCALE,
                -1.0f, 1.0f * SCALE * SCALE,
                1.0f, 1.0f * SCALE * SCALE,
                1.0f, -1.0f * SCALE * SCALE)

        /**
         * 纹理坐标
         */
        private val TEX_VERTEX = floatArrayOf(0f, 1f, 0f, 0f, 1f, 0f, 1f, 1f)

        /**
         * 纹理坐标中每个点占的向量个数
         */
        private const val TEX_VERTEX_COMPONENT_COUNT = 2
    }

    private val mVertexData: FloatBuffer

    private var uTextureUnitLocation: Int = 0
    private val mTexVertexBuffer: FloatBuffer
    private var textureId: Int = 0
    private var surfaceTexture: SurfaceTexture? = null

    init {
        mVertexData = BufferUtil.createFloatBuffer(POINT_DATA)
        mTexVertexBuffer = BufferUtil.createFloatBuffer(TEX_VERTEX)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER)

        val aPositionLocation = getAttrib("a_Position")
        // 纹理坐标索引
        val aTexCoordLocation = getAttrib("a_TexCoord")
        uTextureUnitLocation = getUniform("u_TextureUnit")
        // 纹理数据
        createOESTextureId()

        mVertexData.position(0)
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, mVertexData)
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        // 加载纹理坐标
        mTexVertexBuffer.position(0)
        GLES20.glVertexAttribPointer(aTexCoordLocation, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mTexVertexBuffer)
        GLES20.glEnableVertexAttribArray(aTexCoordLocation)

        GLES20.glClearColor(0f, 0f, 0f, 1f)
        // 开启纹理透明混合，这样才能绘制透明图片
        GLES20.glEnable(GL10.GL_BLEND)
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA)
    }

    private fun createOESTextureId() {
        val textureIds = IntArray(1)
        // 1. 创建纹理对象
        GLES20.glGenTextures(1, textureIds, 0)

        if (textureIds[0] == 0) {
            return
        }
        textureId = textureIds[0]

        // 2. 将纹理绑定到OpenGL对象上
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureIds[0])

        // 3. 设置纹理过滤参数:解决纹理缩放过程中的锯齿问题。若不设置，则会导致纹理为黑色
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT)
        surfaceTexture = SurfaceTexture(textureIds[0])

        surfaceTexture!!.setOnFrameAvailableListener(this)

        val surface = Surface(surfaceTexture)
        mediaPlayer.setSurface(surface)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        GLES20.glViewport(0, 0, width, height)
    }

    private var updateSurface: Boolean = false

    override fun onDrawFrame(glUnused: GL10) {
        if (updateSurface) {
            surfaceTexture!!.updateTexImage()
            updateSurface = false
        }
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)

        GLES20.glUniform1i(uTextureUnitLocation, 0)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, POINT_DATA.size / POSITION_COMPONENT_COUNT)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        updateSurface = true
    }
}
