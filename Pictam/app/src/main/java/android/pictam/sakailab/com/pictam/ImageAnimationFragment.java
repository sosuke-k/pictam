package android.pictam.sakailab.com.pictam;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by taisho6339 on 2014/10/25.
 */
public class ImageAnimationFragment extends Fragment {

    private ImageView mAnimImage;
    private FrameLayout mImageFrame;
    private View mLayout;
    private Handler mHandler = new Handler();
    private int mIndex = 0;

    //カップルアニメーションの画像
    private static final int[] COUPLE_ANIM_IMAGES = {
            R.drawable.couple1,
            R.drawable.couple2,
            R.drawable.couple3,
            R.drawable.couple4,
            R.drawable.couple5,
            R.drawable.couple6,
            R.drawable.couple7,
            R.drawable.couple8,
            R.drawable.couple9,
            R.drawable.couple10,
            R.drawable.couple11,
            R.drawable.couple12,
            R.drawable.couple13,
            R.drawable.couple14,
            R.drawable.couple15,
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_image_anim, null);
        mImageFrame = (FrameLayout) layout.findViewById(R.id.image_container);
        mAnimImage = (ImageView) layout.findViewById(R.id.animation_image);
        mLayout = layout;
        startAnimation();
        return layout;
    }

    private void startAnimation() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIndex >= COUPLE_ANIM_IMAGES.length) {
                    mIndex = 0;
                }
                mAnimImage.setImageResource(COUPLE_ANIM_IMAGES[mIndex++]);
                startAnimation();
            }
        }, 1000);
    }

    //テンプレートマッチングで返されたピクセルの配置場所を元にフレームを移動させる
    public void moveImageFrameByPixels(int i, int j) {
        mLayout.setPadding(i, j, 0, 0);
    }
}
