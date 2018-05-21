package com.benhero.glstudio;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.benhero.glstudio.base.AnimationRenderer;
import com.benhero.glstudio.base.GLAlphaAnimation;
import com.benhero.glstudio.base.GLAnimationListener;
import com.benhero.glstudio.base.GLAnimationSet;
import com.benhero.glstudio.base.GLImageView;
import com.benhero.glstudio.base.GLRotateAnimation;
import com.benhero.glstudio.base.GLScaleAnimation;
import com.benhero.glstudio.base.GLTranslateAnimation;
import com.benhero.glstudio.l1.PointRenderer1_1_1;
import com.benhero.glstudio.l1.PointRenderer1_1_2;
import com.benhero.glstudio.l1.ShapeRenderer1_2;
import com.benhero.glstudio.l2.IndexRenderer2_2;
import com.benhero.glstudio.l2.OrthoRenderer2_1;
import com.benhero.glstudio.l3.ColorfulRenderer3;
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
public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private ViewGroup mRoot;
    private GLSurfaceView mGLSurfaceView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRoot = (ViewGroup) findViewById(R.id.main_root);

        mListView = (ListView) findViewById(R.id.main_list);
        ListAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, MainListItems.ITEMS);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        // 自动点击
        int position = 2;
        mListView.performItemClick(adapter.getView(position, null, null),
                position, adapter.getItemId(position));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGLSurfaceView != null) {
            mGLSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGLSurfaceView != null) {
            mGLSurfaceView.onPause();
        }
    }

    @Override
    public void onBackPressed() {
        if (mGLSurfaceView != null) {
            // 展示了GLSurfaceView，则删除ListView之外的其余控件
            int childCount = mRoot.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (mRoot.getChildAt(i) != mListView) {
                    mRoot.removeViewAt(i);
                    childCount--;
                    i--;
                }
            }
            mGLSurfaceView = null;
        } else {
            super.onBackPressed();
        }
    }

    @NonNull
    private FBORenderer6 chooseFBO6() {
        final ImageView imageView = new ImageView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRoot.addView(imageView, params);
        final FBORenderer6 renderer6 = new FBORenderer6(this);
        renderer6.setCallback(new FBORenderer6.RendererCallback() {
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
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(BitmapFactory.decodeFile(destFile.getPath()));
                            }
                        });
                    }
                }).start();
                data.clear();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderer6.startRenderer();
                mGLSurfaceView.requestRender();
            }
        });
        return renderer6;
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
                mGLSurfaceView.requestRender();
            }
        });
        imageView2.setGLAnimation(alphaAnimation);
        return renderer;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mGLSurfaceView = new GLSurfaceView(this);
        mRoot.addView(mGLSurfaceView);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setEGLConfigChooser(false);

        GLSurfaceView.Renderer renderer;
        switch (position) {
            case 1:
                renderer = new PointRenderer1_1_2(this);
                break;
            case 2:
                renderer = new ShapeRenderer1_2(this);
                break;
            case 3:
                renderer = new OrthoRenderer2_1(this);
                break;
            case 4:
                renderer = new IndexRenderer2_2(this);
                break;
            case 5:
                renderer = new ColorfulRenderer3(this);
                break;
            case 6:
                renderer = new TextureRenderer4(this);
                break;
            case 7:
                renderer = chooseArchitecture5();
                break;
            case 8:
                renderer = chooseFBO6();
                break;
            case 0:
            default:
                renderer = new PointRenderer1_1_1(this);
                break;
        }
        mGLSurfaceView.setRenderer(renderer);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mGLSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGLSurfaceView.requestRender();
            }
        });
    }


}
