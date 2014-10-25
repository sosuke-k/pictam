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
        TextureView.SurfaceTextureListener,
        RetrieveMatchPointWorker.RetrieveMatchPointCallBack {

    private final static int MAX_PREVIEW_COUNT = 10;

    private Camera mCamera = null;
    private Context mContext;
    private int mCount = 0;
    private OnMatchTemplateListener mMatchCallBack = null;

    //プレジュー画像をキャプチャするためのスレッド管理クラスコールバック
    private RetrieveMatchPointWorker mTakePreviewWorker;

    @Override
    public void callBackRetrieve(int i, int j) {
        if (mMatchCallBack == null) {
            return;
        }
        mMatchCallBack.onMatchTemplate(i, j);
    }

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
        setSurfaceTextureListener(this);
    }

    public void addOnMatchTemplateListener(OnMatchTemplateListener listener) {
        mMatchCallBack = listener;
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

    private void initializeCamera(SurfaceTexture surface) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
            setCameraDisplayOrientation();

            //最適なカメラプレビューサイズを取得して適応する
            Camera.Size previewSize = getMiddlePreviewSize();
            Parameters param = mCamera.getParameters();
            param.setPreviewSize(previewSize.width, previewSize.height);

            mCamera.setParameters(param);
            mCamera.setPreviewCallback(mPreviewCallBack);
            mTakePreviewWorker = new RetrieveMatchPointWorker(this);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
                                          int height) {
        //ビューサイズを最大化しておく
        setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        initializeCamera(surface);
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

    //テンプレートマッチングの結果取得コールバック
    public interface OnMatchTemplateListener {
        public void onMatchTemplate(int i, int j);
    }

}