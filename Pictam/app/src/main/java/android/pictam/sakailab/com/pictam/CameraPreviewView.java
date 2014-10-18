package android.pictam.sakailab.com.pictam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.pictam.sakailab.com.pictam.config.Config;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.List;

public class CameraPreviewView extends TextureView implements
        TextureView.SurfaceTextureListener {

    private Camera mCamera = null;
    private Context mContext;

    private int mCount = 0;
    private final static int MAX_PREVIEW_COUNT = 10;

    //プレジュー画像をキャプチャするためのスレッド管理クラスコールバック
    private TakePreviewWorker mTakePreviewWorker;
    private TakePreviewWorker.TakePreviewCallBack mTakePreviewCallBack = new TakePreviewWorker.TakePreviewCallBack() {
        @Override
        public void callBackTakePreview(Bitmap bitmap) {
            Log.d(Config.DEBUG_TAG, "callback create preview bitmap");
        }
    };

    private Camera.PreviewCallback mPreviewCallBack = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (mCount <= MAX_PREVIEW_COUNT) {
                mCount++;
                return;
            }
            mTakePreviewWorker.registerTakePreviewTask(data);
            mCount = 0;
        }
    };

    public CameraPreviewView(Context context) {
        super(context);
        mContext = context;
    }

    private Camera.Size getMiddlePreviewSize() {
        List<Camera.Size> sizeList = mCamera.getParameters()
                .getSupportedPreviewSizes();
        int index = sizeList.size() / 2;
        return sizeList.get(index);
    }

    private void setCameraDisplayOrientation() {
        int degrees = (90 + 360) % 360;
        mCamera.setDisplayOrientation(degrees);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
                                          int height) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
            setCameraDisplayOrientation();

            //最適なカメラプレビューサイズを取得して適応する
            Camera.Size previewSize = getMiddlePreviewSize();
            Parameters param = mCamera.getParameters();
            param.setPreviewSize(previewSize.width, previewSize.height);

            //ビューサイズを最大化しておく
            setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));

            //プレビュー取得の際の画像のピクセルフォーマットをRGBにする
            param.setPreviewFormat(ImageFormat.RGB_565);

            mCamera.setParameters(param);
            mCamera.setPreviewCallback(mPreviewCallBack);

            mTakePreviewWorker = new TakePreviewWorker(mCamera, mTakePreviewCallBack);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
                                            int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        mTakePreviewWorker.unregisterAllTakePreviewTask();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }


}