package android.pictam.sakailab.com.pictam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraPreviewView extends TextureView implements
        TextureView.SurfaceTextureListener {

    private Camera mCamera = null;
    private Context mContext;
    private int mCount = 0;
    private final int MAX_PREVIEW_COUNT = 3;
    private byte[] mImageByteData;

    private class PreviewTakeTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Bitmap bmp = retrievePreviewImage();
            return null;
        }
    }

    private Bitmap retrievePreviewImage() {
        int previewWidth = mCamera.getParameters().getPreviewSize().width;
        int previewHeight = mCamera.getParameters().getPreviewSize().height;
        Bitmap bmp = getBitmapImageFromYUV(mImageByteData, previewWidth, previewHeight);
        return bmp;
    }

    private Bitmap getBitmapImageFromYUV(byte[] data, int width, int height) {
        YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, width, height), 80, baos);
        byte[] jdata = baos.toByteArray();
        BitmapFactory.Options bitmapFatoryOptions = new BitmapFactory.Options();
        bitmapFatoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length, bitmapFatoryOptions);
        return bmp;
    }

    private Camera.PreviewCallback mPreviewCallBack = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (mCount <= MAX_PREVIEW_COUNT) {
                mCount++;
                return;
            }
            mImageByteData = data;
            PreviewTakeTask task = new PreviewTakeTask();
            task.execute();
            mCount = 0;
        }
    };


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
                                          int height) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
            setCameraDisplayOrientation();

            Camera.Size previewSize = getCameraPreviewSize();
            Parameters param = mCamera.getParameters();
            Log.d(Config.DEBUG_TAG, "width:" + previewSize.height + "height:"
                    + previewSize.width);
            setLayoutParams(new FrameLayout.LayoutParams(previewSize.height,
                    previewSize.width, Gravity.CENTER));
            param.setPreviewSize(previewSize.width, previewSize.height);
            mCamera.setParameters(param);
            mCamera.setPreviewCallback(mPreviewCallBack);
        } catch (IOException ioe) {
        }
    }


    //画角の取得
    public float getCameraDegree() {
        if (mCamera != null) {
            Parameters param = mCamera.getParameters();
            return param.getHorizontalViewAngle();
        }
        return -1;
    }

    //仮想スクリーンまでの距離を計算
    public double getVirtualDisplayDistance() {
        float degree = getCameraDegree();
        WindowManager manager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        float xPerInch = metrics.xdpi;//横１インチあたり何ｐｘか
        double tan = Math.tan(Math.toRadians(degree / 2));
        int widthPixel = metrics.widthPixels;//横のピクセル数
        double inch = widthPixel / xPerInch;//画面横のインチ数
        double width = inch * 25.4f;//インチをmmに変換
        double distance = width / 2 / tan;
//        Log.d(Config.DEBUG_TAG, "virtual_distance:" + distance);
        return distance;
    }

    private Camera.Size getCameraPreviewSize() {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point point = new Point();
        disp.getSize(point);
        int dispWidth = point.x;
        int dispHeight = point.y;

        List<Camera.Size> sizeList = mCamera.getParameters()
                .getSupportedPreviewSizes();
        // ビューサイズをカメラのプレビュー画像の大きさにあわせる。
        Camera.Size previewSize = null;
        for (Camera.Size size : sizeList) {
            if ((size.height >= dispWidth) && (size.width >= dispHeight)) {
                if (((size.height - dispWidth) <= 200)
                        && ((size.width - dispHeight) <= 200))
                    previewSize = size;
            }
        }
        if (previewSize == null)
            previewSize = sizeList.get(0);

        return previewSize;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
                                            int height) {
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public CameraPreviewView(Context context) {
        super(context);
        mContext = context;
    }

    private void setCameraDisplayOrientation() {
        int degrees = (90 + 360 - 0) % 360;
        mCamera.setDisplayOrientation(degrees);
    }

}