package com.bt.smart.cargo_owner.activity.userAct;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bt.smart.cargo_owner.BaseActivity;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.utils.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2019/1/8 16:44
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class GetDrivingCardPhotoActivity extends BaseActivity implements View.OnClickListener, Camera.PreviewCallback {
    private SurfaceView sfview;
    private ImageView   img_back;
    private ImageView   img_sure;
    private Camera      mCamera;
    private byte[]      mPicByte;//临时记录预览下的图片数据
    private Bitmap      mBmp;
    private String      fileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_driving_card);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
        setView();
        setData();
    }

    private void setView() {
        sfview = (SurfaceView) findViewById(R.id.sfview);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_sure = (ImageView) findViewById(R.id.img_sure);
    }

    private void setData() {
        img_back.setOnClickListener(this);
        img_sure.setOnClickListener(this);
        //获取前置摄像头，显示在SurfaceView上
        setSurFaceView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_sure:
                //拍摄,获取相机图片
                getCameraPic();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void getCameraPic() {
        mCamera.stopPreview();
        //保存图片
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        try {
            YuvImage image = new YuvImage(mPicByte, ImageFormat.NV21, previewSize.width, previewSize.height, null);
            if (image != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, stream);
                Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                //因为图片会放生旋转，因此要对图片进行旋转到和手机在一个方向上
                rotateMyBitmap(bmp);
                stream.close();
            }
        } catch (Exception ex) {
            Log.e("Sys", "Error:" + ex.getMessage());
        }
        if (null != mBmp) {
            //将bitmap保存，记录照片本地地址，留待之后上传
            boolean b = saveBitmap(mBmp);
            if (b) {
                ToastUtils.showToast(this, "驾驶证保存成功");
                Intent intent = getIntent().putExtra("driving_pic_url", fileUrl);
                setResult(RESULT_FOR_DRIVING_CARD, intent);
                finish();
            } else {
                ToastUtils.showToast(this, "驾驶证获取失败");
            }
        }
    }

    private int RESULT_FOR_DRIVING_CARD = 10108;

    public void rotateMyBitmap(Bitmap bmp) {
        //*****旋转一下
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        mBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }

    private void setSurFaceView() {
        if (mCamera == null) {
            mCamera = Camera.open(0);//后置
        }
        mCamera.setDisplayOrientation(90);
        Camera.Parameters parameters = mCamera.getParameters();
        //后置需要自动对焦，否则采集照片模糊
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(parameters);
        mCamera.startPreview();//开启预览
        mCamera.setPreviewCallback(this);//开启Camera预览回调，重写onPreviewFrame获取相机回调
        mCamera.cancelAutoFocus();//聚焦
        //已打开相机

        final SurfaceHolder mSurfaceHolder = sfview.getHolder();//获取holder参数
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {//设置holder的回调
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                startPreview(mSurfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                stopPreview();
            }
        });

    }

    private void startPreview(SurfaceHolder mSurfaceHolder) {
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPreview() {
        try {
            finish();
        } catch (Exception e) {
        }
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        mPicByte = bytes;
    }

    /**
     * 保存方法
     */
    public boolean saveBitmap(Bitmap bm) {
        long longTime = System.currentTimeMillis();
        fileUrl = "/sdcard/DCIM/Camera/" + longTime + "card002.png";
        //        Log.e(TAG, "保存图片");
        File file = new File("/sdcard/DCIM/Camera/", longTime + "card002.png");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            //保存图片后发送广播通知更新数据库
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            this.sendBroadcast(intent);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
