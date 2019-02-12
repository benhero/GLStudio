package com.benhero.glstudio.util

/**
 * 顶点旋转工具类
 *
 * @author Benhero
 * @date   2019/2/11
 */
object VertexRotationUtil {
    enum class Rotation {
        NORMAL, ROTATION_90, ROTATION_180, ROTATION_270;
    }

    fun getRotation(rotation: Int): Rotation {
        return when (rotation) {
            0 -> VertexRotationUtil.Rotation.NORMAL
            90 -> VertexRotationUtil.Rotation.ROTATION_90
            180 -> VertexRotationUtil.Rotation.ROTATION_180
            270 -> VertexRotationUtil.Rotation.ROTATION_270
            else -> VertexRotationUtil.Rotation.NORMAL
        }
    }

    fun rotate(rotation: VertexRotationUtil.Rotation, srcArray: FloatArray): FloatArray {
        return when (rotation) {
            VertexRotationUtil.Rotation.ROTATION_90 -> floatArrayOf(
                    srcArray[2], srcArray[3],
                    srcArray[4], srcArray[5],
                    srcArray[6], srcArray[7],
                    srcArray[0], srcArray[1])
            VertexRotationUtil.Rotation.ROTATION_180 -> floatArrayOf(
                    srcArray[4], srcArray[5],
                    srcArray[6], srcArray[7],
                    srcArray[0], srcArray[1],
                    srcArray[2], srcArray[3])
            VertexRotationUtil.Rotation.ROTATION_270 -> floatArrayOf(
                    srcArray[6], srcArray[7],
                    srcArray[0], srcArray[1],
                    srcArray[2], srcArray[3],
                    srcArray[4], srcArray[5])
            else -> srcArray
        }
    }
}