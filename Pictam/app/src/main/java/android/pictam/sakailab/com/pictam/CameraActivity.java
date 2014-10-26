package android.pictam.sakailab.com.pictam;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

/**
 * Created by taisho6339 on 2014/10/17.
 */
public class CameraActivity extends FragmentActivity implements
        CameraBridgeViewBase.CvCameraViewListener2,
        RetrieveMatchPointWorker.RetrieveMatchPointCallBack {

    private final static int MAX_PREVIEW_COUNT = 10;
    private int mCount = 0;

    private ImageAnimationFragment mAnimFragment;
    private CameraBridgeViewBase mCameraView;

    private RetrieveMatchPointWorker mTakePreviewWorker;

    //OpenCVManagerと連携して、ライブラリとのリンクが完了後のコールバック
    private BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    setContentView(R.layout.activity_camera_view);
                    initViews();
                    mTakePreviewWorker = new RetrieveMatchPointWorker(CameraActivity.this);
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };


    @Override
    public void onMatchTemplate(int i, int j) {
        mAnimFragment.moveImageFrameByPixels(i, j);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat imageMat = inputFrame.rgba();
        if (mCount <= MAX_PREVIEW_COUNT) {
            mCount++;
            return imageMat;
        }
//        mTakePreviewWorker.registerTakePreviewTask(imageMat);
        mCount = 0;
        return imageMat;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mLoaderCallBack);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //画面が非表示になる時にすべての画像演算処理を停止させる
        mTakePreviewWorker.unregisterAllTakePreviewTask();
    }

    private void initViews() {
        mCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        mCameraView.setCvCameraViewListener(this);
        mCameraView.enableView();
        mAnimFragment = new ImageAnimationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mAnimFragment).commit();
    }


}
