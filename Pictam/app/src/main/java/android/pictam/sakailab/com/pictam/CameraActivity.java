package android.pictam.sakailab.com.pictam;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by taisho6339 on 2014/10/17.
 */
public class CameraActivity extends Activity {

    private FrameLayout mDrawFrame;
    private CameraPreviewView mCameraPreview;
    private ImageView mDummyImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawFrame = new FrameLayout(this);
        mDrawFrame.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        mCameraPreview = new CameraPreviewView(this);
        mDummyImage = new ImageView(this);
        mDummyImage.setBackgroundResource(R.drawable.ic_launcher);
        boolean hasBackCamera = checkHasBackCamera();
        if (hasBackCamera) {
            mCameraPreview.setSurfaceTextureListener(mCameraPreview);
            mDrawFrame.addView(mCameraPreview);
            mDrawFrame.addView(mDummyImage);
        }
        setContentView(mDrawFrame);
        animateRotation(mDummyImage);
    }

    private void animateRotation(ImageView target) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, "rotation", 0f, 360f);
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(1000);
        objectAnimator.start();
    }

    private boolean checkHasBackCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();

        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            int facing = cameraInfo.facing;
            if (facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return true;
            }
        }
        return false;
    }

}
