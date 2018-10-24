package com.benhero.glstudio.renderer

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.Matrix
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.util.ShaderHelper.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 绘制渐变色
 *
 * Created by Herbib on 2018/9/13.
 */
class KotlinRender(context: Context) : BaseRenderer(context) {
    private companion object {
        const val POSITION = "position"
        const val MATRIX = "matrix"
        const val COLOR = "color"
        const val COLOR_FILED = "v_color"
        const val VERTEX_SHADER =
                """attribute vec4 $POSITION;
                    uniform mat4 $MATRIX;
                    attribute vec4 $COLOR;
                    varying vec4 $COLOR_FILED;
                    void main()
                    {
                        gl_Position = $MATRIX * $POSITION;
                        $COLOR_FILED = $COLOR;
                    }"""
        const val FRAG_SHADER =
                """precision mediump float;
                    varying vec4 $COLOR_FILED;
                    void main()
                    {
                        gl_FragColor = $COLOR_FILED;
                    }"""
        const val POSITION_COMPONENT_COUNT = 2
        const val COLOR_COMPONENT_COUNT = 3
        const val FLOAT_SIZE = 4
    }

    private val vertexData: FloatBuffer
    private val positionData = floatArrayOf(
            -.5f, .5f, //4
            .5f, .5f, //1
            -.5f, -.5f, //3
            .5f, -.5f //2
    )
    private val colorBufferData: FloatBuffer
    private val colorData = floatArrayOf(
            1f, 0f, 0f,
            0f, 1f, 0f,
            0f, 0f, 1f,
            .5f, .5f, .5f
    )
    private val matrix = floatArrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
    )
    private var program = 0
    private var positionLocation = 0
    private var matrixLocation = 0
    private var colorLocation = 0

    init {
        vertexData = ByteBuffer
                .allocateDirect(positionData.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        vertexData.put(positionData)

        colorBufferData = ByteBuffer
                .allocateDirect(colorData.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        colorBufferData.put(colorData)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(1F, 1F, 1F, 1F)
        val vertexShader = compileVertexShader(VERTEX_SHADER)
        val fragmentShader = compileFragmentShader(FRAG_SHADER)
        program = linkProgram(vertexShader, fragmentShader)
        glUseProgram(program)

        positionLocation = glGetAttribLocation(program, POSITION)
        matrixLocation = glGetUniformLocation(program, MATRIX)
        colorLocation = glGetAttribLocation(program, COLOR)

        vertexData.position(0)
        glVertexAttribPointer(
                positionLocation,
                POSITION_COMPONENT_COUNT,
                GL_FLOAT,
                false,
                0,
                vertexData)
        glEnableVertexAttribArray(positionLocation)

        colorBufferData.position(0)
        glVertexAttribPointer(
                colorLocation,
                COLOR_COMPONENT_COUNT,
                GL_FLOAT,
                false,
                0,
                colorBufferData
        )
        glEnableVertexAttribArray(colorLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        val radius: Float = if (width > height) width.toFloat() / height else height.toFloat() / width
        if (width > height) {
            Matrix.orthoM(matrix, 0, -radius, radius, -1f, 1f, -1f, 1f)
        } else {
            Matrix.orthoM(matrix, 0, -1f, 1f, -radius, radius, -1f, 1f)
        }
        glUniformMatrix4fv(matrixLocation, 1, false, matrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
    }
}