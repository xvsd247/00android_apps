package com.zl.dualcamera;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "DualCam";
    private Camera mCamera1;
    private Camera mCamera2;
    private SurfaceView mSurfaceView1;
    private SurfaceHolder mSurfaceHolder1;
    private SurfaceView mSurfaceView2;
    private SurfaceHolder mSurfaceHolder2;
    private Button mOpen1;
    private Button mOpen2;
    private int previewSizeWidth = 640;
    private int previewSizeHeight = 480;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOpen1 = (Button)findViewById(R.id.button);
        mOpen2 = (Button)findViewById(R.id.button1);
        mSurfaceView1 = (SurfaceView)findViewById(R.id.surfaceView);
        mSurfaceHolder1 = mSurfaceView1.getHolder();// 取得holder
        mSurfaceHolder1.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "camera1 surface created");
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.d(TAG, "camera1 surface changed");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "camera1 surface destroyed");
                releaseCameraAndPreview(0);
            }
        });


        mSurfaceView2 = (SurfaceView)findViewById(R.id.surfaceView1);
        mSurfaceHolder2 = mSurfaceView2.getHolder();// 取得holder
        mSurfaceHolder2.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "camera2 surface created");

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.d(TAG, "camera2 surface changed");

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "camera2 surface destroyed");
                releaseCameraAndPreview(1);

            }
        });

        mOpen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraOpen(0);
            }
        });
        mOpen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraOpen(1);
            }
        });
        Log.d(TAG, "camera number: " + Camera.getNumberOfCameras());
        if(Camera.getNumberOfCameras() <= 1) {
            mOpen2.setClickable(false);
        }
    }

    private void cameraOpen(int id) {
        releaseCameraAndPreview(id);

        if(id == 0) {
            try {
                mCamera1 = Camera.open(id);

            } catch (Exception e) {
                Log.e(TAG, "failed to open Camera");
                e.printStackTrace();
            }
            if(mCamera1 != null) {
                Camera.Parameters params = mCamera1.getParameters();
                params.setPreviewSize(previewSizeWidth, previewSizeHeight);
                params.setPictureFormat(PixelFormat.JPEG);
                params.setPictureSize(previewSizeWidth, previewSizeHeight);
                mCamera1.setParameters(params);
                try {
                    mCamera1.setPreviewDisplay(mSurfaceHolder1);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.e(TAG, "preview failed.");
                    e.printStackTrace();
                }
                mCamera1.startPreview();
            }
        } else if(id == 1) {
            try {
                mCamera2 = Camera.open(id);

            } catch (Exception e) {
                //Log.e(getString(R.string.app_name), "failed to open Camera");
                Log.e(TAG, "failed to open Camera");
                e.printStackTrace();
            }
            if (mCamera2 != null) {
                Camera.Parameters params = mCamera2.getParameters();
                params.setPreviewSize(previewSizeWidth, previewSizeHeight);
                params.setPictureFormat(PixelFormat.JPEG);
                params.setPictureSize(previewSizeWidth, previewSizeHeight);
                mCamera2.setParameters(params);
                try {
                    mCamera2.setPreviewDisplay(mSurfaceHolder2);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.e(TAG, "preview failed.");
                    e.printStackTrace();
                }
                mCamera2.startPreview();
            }
        }
    }

    private void releaseCameraAndPreview(int id) {
        if (id == 0) {
            if (mCamera1 != null) {
                mCamera1.stopPreview();
                mCamera1.release();
                mCamera1 = null;
            }
        } else if(id == 1) {
            if (mCamera2 != null) {
                mCamera2.stopPreview();
                mCamera2.release();
                mCamera2 = null;
            }
        }
    }

}