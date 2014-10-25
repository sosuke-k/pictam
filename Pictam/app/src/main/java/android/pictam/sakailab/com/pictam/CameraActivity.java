package android.pictam.sakailab.com.pictam;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by taisho6339 on 2014/10/17.
 */
public class CameraActivity extends FragmentActivity implements CameraPreviewView.OnMatchTemplateListener {

    private ImageAnimationFragment mAnimFragment;

    @Override
    public void onMatchTemplate(int i, int j) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        initViews();
    }

    private void initViews() {
        FrameLayout cameraFrame = (FrameLayout) findViewById(R.id.camera_container);
        CameraPreviewView cameraPreview = new CameraPreviewView(this);
        boolean hasBackCamera = checkHasBackCamera();
        if (hasBackCamera) {
            cameraPreview.addOnMatchTemplateListener(this);
            cameraFrame.addView(cameraPreview);
        }
        mAnimFragment = new ImageAnimationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mAnimFragment).commit();
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
