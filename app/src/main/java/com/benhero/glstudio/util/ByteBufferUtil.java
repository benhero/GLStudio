package com.benhero.glstudio.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Buffer工具类
 *
 * @author Benhero
 */
public class ByteBufferUtil {
    /**
     * Float类型占4Byte
     */
    private static final int BYTES_PER_FLOAT = 4;

    /**
     * 创建一个FloatBuffer
     */
    public static FloatBuffer createFloatBuffer(float[] array) {
        FloatBuffer buffer = ByteBuffer
                // 分配顶点坐标分量个数 * Float占的Byte位数
                .allocateDirect(array.length * BYTES_PER_FLOAT)
                // 按照本地字节序排序
                .order(ByteOrder.nativeOrder())
                // Byte类型转Float类型
                .asFloatBuffer();

        // 将Dalvik的内存数据复制到Native内存中
        buffer.put(array);
        return buffer;
    }
}
