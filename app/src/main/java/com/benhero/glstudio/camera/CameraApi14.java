package com.benhero.glstudio.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * for api 14
 * <p>
 * Camera主要涉及参数
 * 1. 预览画面的大小
 * 2. pic图片的大小
 * 3. 对焦模式
 * 4. 闪光灯模式
 * <p>
 * 需要需要注意相机旋转方向的问题
 * 1. 通过设置相机的displayOritation 和 pramaras中的旋转方向。主要它可以保存在选摄像头的时候的参数中
 * 2. 因为预览图是 通过SurfaceTextureView 中显示。可以设置matrix来控制它的旋转。  如这里是手动去控制纹理的绘制的话，则可以自己控制viewMatrix来控制
 */
public class CameraApi14 implements ICamera {
    private static final String TAG = "CameraApi14";
    public CameraSize mPreviewSize;
    public CameraSize mPicSize;
    /*
    当前的相机Id
     */
    private int mCameraId;
    /*
    当前的相机对象
     */
    private Camera mCamera;
    /*
    当前的相机参数
     */
    private Camera.Parameters mCameraParameters;
    /**
     * 相机参数
     */
    private Camera.CameraInfo mCameraInfo;
    //想要的尺寸。
    private int mDesiredWidth = 1080;
    private int mDesiredHeight = 1920;
    private boolean mAutoFocus;
    /*
     * 当前相机的高宽比
     */
    private AspectRatio mDesiredAspectRatio;
    private AtomicBoolean isPictureCaptureInProgress = new AtomicBoolean(false);
    private TakePhotoCallback photoCallBack;


    public CameraApi14() {
        mDesiredWidth = 1080;
        mDesiredHeight = 1920;
        //创建默认的比例.因为后置摄像头的比例，默认的情况下，都是旋转了270
        mDesiredAspectRatio = AspectRatio.of(mDesiredWidth, mDesiredHeight).inverse();
    }

    @Override
    public boolean open(int cameraId) {
        /*
            预览的尺寸和照片的尺寸
        */
        final CameraSize.ISizeMap mPreviewSizes = new CameraSize.ISizeMap();
        final CameraSize.ISizeMap mPictureSizes = new CameraSize.ISizeMap();
        if (mCamera != null) {
            releaseCamera();
        }
        mCameraId = cameraId;
        mCamera = Camera.open(mCameraId);
        mCameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, mCameraInfo);
        if (mCamera != null) {
            mCameraParameters = mCamera.getParameters();

            mPreviewSizes.clear();
            //先收集参数.因为每个手机能够得到的摄像头参数都不一致。所以将可能的尺寸都得到。
            for (Camera.Size size : mCameraParameters.getSupportedPreviewSizes()) {
                mPreviewSizes.add(new CameraSize(size.width, size.height));
            }

            mPictureSizes.clear();
            for (Camera.Size size : mCameraParameters.getSupportedPictureSizes()) {
                mPictureSizes.add(new CameraSize(size.width, size.height));
            }
            //挑选出最需要的参数
            adJustParametersByAspectRatio2(mPreviewSizes, mPictureSizes);
            return true;
        }
        return false;
    }

    private void adJustParametersByAspectRatio(CameraSize.ISizeMap previewSizes, CameraSize.ISizeMap pictureSizes) {
        //得到当前预期比例的size
        SortedSet<CameraSize> sizes = previewSizes.sizes(mDesiredAspectRatio);
        if (sizes == null) {  //表示不支持.
            // TODO: 2018/9/14 这里应该抛出异常？
            return;
        }
        //当前先不考虑Orientation
        CameraSize previewSize;
        mPreviewSize = new CameraSize(mDesiredWidth, mDesiredHeight);
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mPreviewSize = new CameraSize(mDesiredHeight, mDesiredWidth);
            mCameraParameters.setRotation(90);
        } else {
            mPreviewSize = new CameraSize(mDesiredHeight, mDesiredWidth);
        }

        //默认去取最大的尺寸
        mPicSize = pictureSizes.sizes(mDesiredAspectRatio).first();

        mCameraParameters.setPreviewSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        mCameraParameters.setPictureSize(mPicSize.getWidth(), mPicSize.getHeight());

        //设置对角和闪光灯
        setAutoFocusInternal(mAutoFocus);
        //先不设置闪光灯
//        mCameraParameters.setFlashMode("FLASH_MODE_OFF");

        //设置到camera中
//        mCameraParameters.setRotation(90);
        mCamera.setParameters(mCameraParameters);
//        mCamera.setDisplayOrientation(90);
//        setCameraDisplayOrientation();
    }

    private void adJustParametersByAspectRatio2(CameraSize.ISizeMap previewSizes, CameraSize.ISizeMap pictureSizes) {
        //得到当前预期比例的size
        SortedSet<CameraSize> sizes = previewSizes.sizes(mDesiredAspectRatio);
        if (sizes == null) {  //表示不支持.
            // TODO: 2018/9/14 这里应该抛出异常？
            return;
        }
        for (CameraSize next : sizes) {
            if (next.getWidth() >= 720 && next.getHeight() >= 720) {
                mPreviewSize = next;
                break;
            }
        }
        if (mPreviewSize == null) {
            mPreviewSize = sizes.last();
        }

//
        for (CameraSize next : pictureSizes.sizes(mDesiredAspectRatio)) {
            if (next.getWidth() >= 720 && next.getHeight() >= 720) {
                mPicSize = next;
                break;
            }
        }
        if (mPicSize == null) {
            mPicSize = pictureSizes.sizes(mDesiredAspectRatio).last();
        }

        mCameraParameters.setPreviewSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        mCameraParameters.setPictureSize(mPicSize.getWidth(), mPicSize.getHeight());

        mPreviewSize = mPreviewSize.inverse();
        mPicSize = mPicSize.inverse();

        Log.d(TAG, "preview=" + mPreviewSize);
        Log.d(TAG, "mPicSize=" + mPicSize);
        //设置对角和闪光灯
        setAutoFocusInternal(mAutoFocus);
        //先不设置闪光灯
//        mCameraParameters.setFlashMode("FLASH_MODE_OFF");

        //设置到camera中
//        mCameraParameters.setRotation(90);
        mCamera.setParameters(mCameraParameters);
//        mCamera.setDisplayOrientation(90);
//        setCameraDisplayOrientation();
    }

    private boolean setAutoFocusInternal(boolean autoFocus) {
        mAutoFocus = autoFocus;
//        if (isCameraOpened()) {
        final List<String> modes = mCameraParameters.getSupportedFocusModes();
        if (autoFocus && modes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (modes.contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
        } else if (modes.contains(Camera.Parameters.FOCUS_MODE_INFINITY)) {
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        } else {
            mCameraParameters.setFocusMode(modes.get(0));
        }
        return true;
//        } else {
//            return false;
//        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        this.mDesiredAspectRatio = aspectRatio;
    }

    @Override
    public boolean preview() {
        if (mCamera != null) {
            mCamera.startPreview();
            return true;
        }
        return false;
    }

    @Override
    public boolean close() {
        if (mCamera != null) {
            try {
                //stop preview时，可能爆出异常
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public void setPreviewTexture(SurfaceTexture surfaceTexture) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewTexture(surfaceTexture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CameraSize getPreviewSize() {
        return mPreviewSize;
    }

    @Override
    public CameraSize getPictureSize() {
        return mPicSize;
    }

    @Override
    public int getRotation() {
        return mCameraInfo.orientation;
    }

    @Override
    public boolean isFront() {
        return mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

    @Override
    public void takePhoto(TakePhotoCallback callback) {
        this.photoCallBack = callback;
        if (getAutoFocus()) {
            mCamera.cancelAutoFocus();
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    takePictureInternal();
                }
            });
        } else {
            takePictureInternal();
        }

    }

    private void takePictureInternal() {
        if (!isPictureCaptureInProgress.getAndSet(true)) {
            final long start = System.currentTimeMillis();
            mCamera.takePicture(null, null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    long end = System.currentTimeMillis();
                    Log.d(TAG, "mCamera.takePicture cost = " + (end - start));
                    isPictureCaptureInProgress.set(false);
                    if (photoCallBack != null) {
                        photoCallBack.onTakePhoto(data, mPreviewSize.getWidth(), mPreviewSize.getHeight());
                    }
                    camera.cancelAutoFocus();
                    camera.startPreview();
                }
            });
        }
    }

    //判断是否自动对焦
    private boolean getAutoFocus() {
        String focusMode = mCameraParameters.getFocusMode();
        return focusMode != null && focusMode.contains("continuous");
    }
}