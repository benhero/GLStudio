package com.benhero.glstudio.camera;

import android.graphics.SurfaceTexture;

/**
 * 定义个相机的功能接口
 */
public interface ICamera {
    boolean open(int cameraId);

    /**
     * 设置画面的比例
     */
    void setAspectRatio(AspectRatio aspectRatio);

    /**
     * 开启预览
     */
    boolean preview();

    /**
     * 关闭相机
     *
     * @return
     */
    boolean close();

    /**
     * 使用SurfaceTexture 来作为预览的画面
     *
     * @param surfaceTexture
     */
    void setPreviewTexture(SurfaceTexture surfaceTexture);

    CameraSize getPreviewSize();

    CameraSize getPictureSize();

    int getRotation();

    boolean isFront();

    /**
     * 添加拍照的功能
     */
    void takePhoto(TakePhotoCallback callback);

    /**
     * 通过回调，将对应的数据传递回去
     */
    interface TakePhotoCallback {
        void onTakePhoto(byte[] bytes, int width, int height);
    }
}