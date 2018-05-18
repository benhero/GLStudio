package com.benhero.glstudio;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.benhero.glstudio.base.AnimationRenderer;
import com.benhero.glstudio.base.GLAlphaAnimation;
import com.benhero.glstudio.base.GLAnimationListener;
import com.benhero.glstudio.base.GLAnimationSet;
import com.benhero.glstudio.base.GLImageView;
import com.benhero.glstudio.base.GLRotateAnimation;
import com.benhero.glstudio.base.GLScaleAnimation;
import com.benhero.glstudio.base.GLTranslateAnimation;
import com.benhero.glstudio.l1.PointRenderer1_1_2;
import com.benhero.glstudio.l1.ShapeRenderer1_2;
import com.benhero.glstudio.l2.OrthoRenderer2_1;
import com.benhero.glstudio.l4.TextureRenderer4;
import com.benhero.glstudio.l5.Architecture5;
import com.benhero.glstudio.l6.FBORenderer6;
import com.jayfeng.lesscode.core.BitmapLess;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 主界面
 *
 * @author Benhero
 */
public class MainActivity extends Activity {

    private GLSurfaceView mGLSurfaceView;
    private ImageView mImageView;
    private FBORenderer6 mFboRenderer6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.main_gl_surface);
        mImageView = (ImageView) findViewById(R.id.main_iv);

        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setEGLConfigChooser(false);

        GLSurfaceView.Renderer renderer = chooseLesson(PointRenderer1_1_2.class);
        mGLSurfaceView.setRenderer(renderer);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    private GLSurfaceView.Renderer chooseLesson(Class<? extends GLSurfaceView.Renderer> className) {
        if (className.equals(Architecture5.class)) {
            return chooseArchitecture5();
        } else if (className.equals(FBORenderer6.class)) {
            return chooseFBO6();
        } else if (className.equals(TextureRenderer4.class)) {
            return new TextureRenderer4(this);
        } else if (className.equals(ShapeRenderer1_2.class)) {
            return new ShapeRenderer1_2(this);
        } else if(className.equals(OrthoRenderer2_1.class)) {
            return new OrthoRenderer2_1(this);
        }
        return new PointRenderer1_1_2(this);
    }

    @NonNull
    private FBORenderer6 chooseFBO6() {
        if (mFboRenderer6 != null) {
            return mFboRenderer6;
        }
        mFboRenderer6 = new FBORenderer6(this);
        mFboRenderer6.setCallback(new FBORenderer6.RendererCallback() {
            @Override
            public void onRendererDone(ByteBuffer data, int width, int height) {
                final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Log.i("JKL", "MainActivity - onRendererDone: " + data.capacity() + " : " + bitmap.getByteCount());
                bitmap.copyPixelsFromBuffer(data);
                final File destFile = new File("/sdcard/A/test"
//                        + String.valueOf(System.currentTimeMillis())
                        + ".jpg");
                try {
                    new File("/sdcard/A").mkdirs();
                    destFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BitmapLess.$save(bitmap, Bitmap.CompressFormat.JPEG, 100, destFile);
                        mImageView.post(new Runnable() {
                            @Override
                            public void run() {
                                mImageView.setImageBitmap(BitmapFactory.decodeFile(destFile.getPath()));
                            }
                        });
                    }
                }).start();
                data.clear();
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setImageResource(0);
                mFboRenderer6.startRenderer();
                mGLSurfaceView.requestRender();
            }
        });
        return mFboRenderer6;
    }

    @NonNull
    private GLSurfaceView.Renderer chooseArchitecture5() {
        AnimationRenderer renderer = new Architecture5(this);
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
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        long now = System.currentTimeMillis();

        // 位移
        GLTranslateAnimation translateAnimation = new GLTranslateAnimation();
        translateAnimation.setFromX(500);
        translateAnimation.setFromY(0);
        translateAnimation.setToX(500);
        translateAnimation.setToY(1400);
        translateAnimation.setInterpolator(new DecelerateInterpolator(2f));

        // 缩放
        GLScaleAnimation scaleAnimation = new GLScaleAnimation();
        scaleAnimation.setFromX(0.5f);
        scaleAnimation.setFromY(0.5f);
        scaleAnimation.setToX(2.0f);
        scaleAnimation.setToY(2.0f);
        scaleAnimation.setInterpolator(new DecelerateInterpolator(2f));

        // 旋转
        GLRotateAnimation rotateAnimation = new GLRotateAnimation();
        rotateAnimation.setFromDegrees(0);
        rotateAnimation.setToDegrees(360);
        rotateAnimation.setInterpolator(new OvershootInterpolator());

        GLAnimationSet set = new GLAnimationSet();
        set.addAnimation(translateAnimation);
        set.addAnimation(rotateAnimation);
        set.addAnimation(alphaAnimation);
        set.addAnimation(scaleAnimation);
        imageView.setGLAnimation(set);

        set.setStartTime(now);
        set.setDuration(3000);
        set.setListener(new GLAnimationListener() {
            @Override
            public void onStart() {
                Log.e("JKL", "onStart: ");
            }

            @Override
            public void onEnd() {
                Log.e("JKL", "onEnd: ");
            }

            @Override
            public void onProgress(float percent) {
                Log.i("JKL", "onProgress: " + percent);
            }
        });
        imageView2.setGLAnimation(alphaAnimation);
        return renderer;
    }
}
