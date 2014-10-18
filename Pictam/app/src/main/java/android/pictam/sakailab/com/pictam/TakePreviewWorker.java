package android.pictam.sakailab.com.pictam;

import android.graphics.Bitmap;
import android.hardware.Camera;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by taisho6339 on 2014/10/17.
 */
public class TakePreviewWorker {

    private Camera mCamera;
    private ExecutorService mWorker = Executors.newSingleThreadExecutor();
    private TakePreviewCallBack mCallBack;

    class TakePreviewTask implements Runnable {
        private byte[] mImageData;

        TakePreviewTask(byte[] data) {
            mImageData = data;
        }

        @Override
        public void run() {
            //TODO
            //NDKにbyte配列を渡して座標を取得
            //コールバック

//            mCallBack.callBackTakePreview(bitmap);
        }
    }

    public void registerTakePreviewTask(byte[] data) {
        TakePreviewTask task = new TakePreviewTask(data);
        mWorker.submit(task);
    }

    public void unregisterAllTakePreviewTask() {
        try {
            mWorker.shutdown();

            if (!mWorker.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                mWorker.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("awaitTermination interrupted: " + e);
            mWorker.shutdownNow();
        }
    }

    public TakePreviewWorker(Camera camera, TakePreviewCallBack callBack) {
        mCamera = camera;
        mCallBack = callBack;
    }

    static interface TakePreviewCallBack {
        public void callBackTakePreview(Bitmap bitmap);
    }
}
