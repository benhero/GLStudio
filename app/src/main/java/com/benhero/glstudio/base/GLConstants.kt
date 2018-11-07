package com.benhero.glstudio.base

/**
 * GL架构使用的常量
 *
 * @author Benhero
 */
object GLConstants {
    /**
     * 纹理坐标个数
     */
    val POSITION_COUNT = 4
    /**
     * 纹理坐标关联的分量个数
     */
    val POSITION_COMPONENT_COUNT = 2
    /**
     * 纹理混合颜色关联的分量个数：RGBA
     */
    val COLOR_COMPONENT_COUNT = 4
    /**
     * 纹理混合颜色数组长度
     */
    val COLOR_COUNT = 16
    /**
     * Float类型占4Byte
     */
    val BYTES_PER_FLOAT = 4
    /**
     * 纹理坐标中每个点占的向量个数
     */
    val TEX_VERTEX_COMPONENT_COUNT = 2
}
