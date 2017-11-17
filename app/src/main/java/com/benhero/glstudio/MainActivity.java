package com.benhero.glstudio;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.DecelerateInterpolator;

import com.benhero.glstudio.base.BaseRenderer;
import com.benhero.glstudio.base.GLAlphaAnimation;
import com.benhero.glstudio.base.GLImageView;
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
        imageView.setX(300);
        imageView.setY(1920);
        imageView.setAlpha(1f);
        renderer.addGLImageView(imageView);

        GLImageView imageView2 = new GLImageView();
        imageView2.setResId(R.mipmap.ic_launcher);
        imageView2.setX(200);
        imageView2.setY(1920);
        imageView2.setAlpha(1f);
        renderer.addGLImageView(imageView2);

        GLAlphaAnimation alphaAnimation = new GLAlphaAnimation();
        alphaAnimation.setFromAlpha(0f);
        alphaAnimation.setToAlpha(1f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setStartTime(System.currentTimeMillis() + 1000);
        alphaAnimation.setEndTime(System.currentTimeMillis() + 2000);

        GLTranslateAnimation translateAnimation = new GLTranslateAnimation();
        translateAnimation.setDuration(1000);
        translateAnimation.setFromX(200);
        translateAnimation.setFromY(1620);
        translateAnimation.setToX(200);
        translateAnimation.setToY(0);
        translateAnimation.setDuration(2000);
        translateAnimation.setStartTime(System.currentTimeMillis() + 1000);
        translateAnimation.setEndTime(System.currentTimeMillis() + 3000);
        translateAnimation.setInterpolator(new DecelerateInterpolator(2f));

        imageView.setGLAnimation(translateAnimation);
//        imageView2.setGLAnimation(alphaAnimation);

        mGLSurfaceView.setRenderer(renderer);
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
