package com.benhero.glstudio;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.benhero.glstudio.base.BaseRenderer;
import com.benhero.glstudio.base.GLAlphaAnimation;
import com.benhero.glstudio.base.GLAnimationListener;
import com.benhero.glstudio.base.GLAnimationSet;
import com.benhero.glstudio.base.GLImageView;
import com.benhero.glstudio.base.GLRotateAnimation;
import com.benhero.glstudio.base.GLScaleAnimation;
import com.benhero.glstudio.base.GLTranslateAnimation;
import com.benhero.glstudio.l5.Architecture7;

/**
 * 主界面
 *
 * @author Benhero
 */
public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setEGLConfigChooser(false);
        BaseRenderer renderer = new Architecture7(this);
        GLImageView imageView = new GLImageView();
        imageView.setResId(R.drawable.tuzki);
        imageView.setX(400);
        imageView.setY(400);
        imageView.setAlpha(1f);
        renderer.addGLImageView(imageView);

        GLImageView imageView2 = new GLImageView();
        imageView2.setResId(R.mipmap.ic_launcher);
        imageView2.setX(200);
        imageView2.setY(1920);
        imageView2.setAlpha(1f);
//        renderer.addGLImageView(imageView2);

        // 透明度
        GLAlphaAnimation alphaAnimation = new GLAlphaAnimation();
        alphaAnimation.setFromAlpha(0.5f);
        alphaAnimation.setToAlpha(1f);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        alphaAnimation.setStartTime(System.currentTimeMillis());
        alphaAnimation.setEndTime(System.currentTimeMillis() + 3000);

        // 位移
        GLTranslateAnimation translateAnimation = new GLTranslateAnimation();
        translateAnimation.setFromX(500);
        translateAnimation.setFromY(0);
        translateAnimation.setToX(500);
        translateAnimation.setToY(1400);
        translateAnimation.setDuration(3000);
        translateAnimation.setStartTime(System.currentTimeMillis());
        translateAnimation.setEndTime(System.currentTimeMillis() + 3000);
        translateAnimation.setInterpolator(new DecelerateInterpolator(2f));

        // 缩放
        GLScaleAnimation scaleAnimation = new GLScaleAnimation();
        scaleAnimation.setDuration(2000);
        scaleAnimation.setToX(2.0f);
        scaleAnimation.setToY(2.0f);
        scaleAnimation.setInterpolator(new DecelerateInterpolator(2f));
        scaleAnimation.setStartTime(System.currentTimeMillis());
        scaleAnimation.setEndTime(System.currentTimeMillis() + 2000);

        // 旋转
        GLRotateAnimation rotateAnimation = new GLRotateAnimation();
        rotateAnimation.setFromDegrees(0);
        rotateAnimation.setToDegrees(360);
        rotateAnimation.setDuration(3000);
        rotateAnimation.setInterpolator(new OvershootInterpolator());
        rotateAnimation.setStartTime(System.currentTimeMillis());
        rotateAnimation.setEndTime(System.currentTimeMillis() + 3000);

        GLAnimationSet set = new GLAnimationSet();
        set.addAnimation(translateAnimation);
        set.addAnimation(rotateAnimation);
        set.addAnimation(alphaAnimation);
        imageView.setGLAnimation(set);
        imageView2.setGLAnimation(alphaAnimation);

        renderer.setAnimationListener(new GLAnimationListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd() {

            }

            @Override
            public void onProgress(float percent) {
                mGLSurfaceView.requestRender();
            }
        });
        mGLSurfaceView.setRenderer(renderer);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }
}
