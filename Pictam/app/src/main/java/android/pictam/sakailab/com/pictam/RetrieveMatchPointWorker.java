package android.pictam.sakailab.com.pictam;

import android.os.Handler;

import org.opencv.core.Mat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by taisho6339 on 2014/10/17.
 */
public class RetrieveMatchPointWorker {

    //ワーカースレッドを実際に操作するメンバ。常にシングルスレッド。
    private ExecutorService mWorker = Executors.newSingleThreadExecutor();
    private RetrieveMatchPointCallBack mCallBack;
    private TemplateMatch mTempMatcher = new TemplateMatch();
    private Handler mHandler = new Handler();

    private class TakePreviewTask implements Runnable {
        private Mat mImageData;

        TakePreviewTask(Mat data) {
            mImageData = data;
        }

        @Override
        public void run() {
            mTempMatcher.setSearchImg(mImageData);
            final int[] point = mTempMatcher.match();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallBack.onMatchTemplate(point[0], point[1]);
                }
            });
        }
    }

    public RetrieveMatchPointWorker(RetrieveMatchPointCallBack callBack) {
        mCallBack = callBack;
    }

    public void registerTakePreviewTask(Mat data) {
        if (mWorker.isShutdown()) {
            return;
        }
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
            e.printStackTrace();
            mWorker.shutdownNow();
        }
    }


    static interface RetrieveMatchPointCallBack {
        //マッチした画像上の位置座標を返すメソッド
        public void onMatchTemplate(int i, int j);
    }
}
