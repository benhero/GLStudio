/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.benhero.glstudio.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;


/**
 * 纹理加载助手类
 */
public class TextureHelper {
    private static final String TAG = "TextureHelper";

    /**
     * 根据资源ID获取相应的OpenGL纹理ID，若加载失败则返回0
     * <br>必须在GL线程中调用
     */
    public static TextureBean loadTexture(Context context, int resourceId) {
        TextureBean bean = new TextureBean();
        final int[] textureObjectIds = new int[1];
        // 1. 创建纹理对象
        GLES20.glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not generate a new OpenGL texture object.");
            }
            return bean;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), resourceId, options);

        if (bitmap == null) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
            }
            // 加载Bitmap资源失败，删除纹理Id
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return bean;
        }
        // 2. 将纹理绑定到OpenGL对象上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);

        // 3. 设置纹理过滤参数:解决纹理缩放过程中的锯齿问题。若不设置，则会导致纹理为黑色
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        // 4. 通过OpenGL对象读取Bitmap数据，并且绑定到纹理对象上，之后就可以回收Bitmap对象
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.
        // 5. 生成Mip位图
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        // 6. 回收Bitmap对象
        bean.setWidth(bitmap.getWidth());
        bean.setHeight(bitmap.getHeight());
        bitmap.recycle();

        // 7. 将纹理从OpenGL对象上解绑
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        // 所以整个流程中，OpenGL对象类似一个容器或者中间者的方式，将Bitmap数据转移到OpenGL纹理上
        bean.setTextureId(textureObjectIds[0]);
        return bean;
    }

    /**
     * 纹理数据
     */
    public static class TextureBean {
        private int mTextureId;
        private int mWidth;
        private int mHeight;

        public int getTextureId() {
            return mTextureId;
        }

        public void setTextureId(int textureId) {
            mTextureId = textureId;
        }

        public int getWidth() {
            return mWidth;
        }

        public void setWidth(int width) {
            mWidth = width;
        }

        public int getHeight() {
            return mHeight;
        }

        public void setHeight(int height) {
            mHeight = height;
        }
    }
}
