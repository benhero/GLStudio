package com.benhero.glstudio.base;

/**
 * OpenGL绘制对象工具类
 *
 * @author Benhero
 */
public class GLObjectUtils {

    public static void setToCenter(GLObject object) {
        setToCenterX(object);
        setToCenterY(object);
    }

    public static void setToCenterX(GLObject object) {
        object.setX((object.getParentWidth() - object.getWidth()) / 2);
    }

    public static void setToCenterY(GLObject object) {
        object.setY((object.getParentHeight() - object.getHeight()) / 2);
    }
}
