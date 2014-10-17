package android.pictam.sakailab.com.pictam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import java.io.ByteArrayOutputStream;
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
            Bitmap bitmap = retrievePreviewImage(mImageData);
            mCallBack.callBackTakePreview(bitmap);
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

    private Bitmap retrievePreviewImage(byte[] data) {
        int previewWidth = mCamera.getParameters().getPreviewSize().width;
        int previewHeight = mCamera.getParameters().getPreviewSize().height;
        return getBitmapImageFromYUV(data, previewWidth, previewHeight);
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

    static interface TakePreviewCallBack {
        public void callBackTakePreview(Bitmap bitmap);
    }
}
