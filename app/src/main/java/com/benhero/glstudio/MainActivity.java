package com.benhero.glstudio;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
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
import android.widget.Toast;

import com.benhero.glstudio.base.GLAlphaAnimation;
import com.benhero.glstudio.base.GLAnimationListener;
import com.benhero.glstudio.base.GLAnimationSet;
import com.benhero.glstudio.base.GLImageView;
import com.benhero.glstudio.base.GLRotateAnimation;
import com.benhero.glstudio.base.GLScaleAnimation;
import com.benhero.glstudio.base.GLTranslateAnimation;
import com.benhero.glstudio.renderer.L6_Architecture;
import com.benhero.glstudio.renderer.L7_FBORenderer;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jayfeng.lesscode.core.BitmapLess;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

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
        int position = 3;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mGLSurfaceView = new GLSurfaceView(this);
        mRoot.addView(mGLSurfaceView);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setEGLConfigChooser(false);

        Class clickClass = MainListItems.getClass(position);
        GLSurfaceView.Renderer renderer = MainListItems.getRenderer(clickClass, this);
        if (renderer == null) {
            Toast.makeText(this, "反射构建渲染器失败", Toast.LENGTH_SHORT).show();
            return;
        }

        if (clickClass == L6_Architecture.class) {
            chooseArchitecture5((L6_Architecture) renderer);
        } else if (clickClass == L7_FBORenderer.class) {
            chooseFBO6((L7_FBORenderer) renderer);
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

    private void chooseArchitecture5(L6_Architecture renderer) {
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
    }

    private void chooseFBO6(final L7_FBORenderer renderer) {
        final ImageView imageView = new ImageView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRoot.addView(imageView, params);
        renderer.setCallback(new L7_FBORenderer.RendererCallback() {
            @Override
            public void onRendererDone(ByteBuffer data, int width, int height) {
                final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Log.i("JKL", "MainActivity - onRendererDone: " + data.capacity() + " : " + bitmap.getByteCount());
                bitmap.copyPixelsFromBuffer(data);
                final File destFile = new File("/sdcard/A/test"
//                        + String.valueOf(System.currentTimeMillis())
                        + ".jpg");
                TedPermission.with(MainActivity.this)
                        .setPermissionListener(mPermissionListener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
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
                renderer.startRenderer();
                mGLSurfaceView.requestRender();
            }
        });
    }

    PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };
}
