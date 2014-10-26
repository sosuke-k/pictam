package android.pictam.sakailab.com.pictam;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by taisho6339 on 2014/10/25.
 */
public class ImageAnimationFragment extends Fragment {

    private ImageView mAnimImage;
    private FrameLayout mImageFrame;
    private View mLayout;
    private Handler mHandler = new Handler();
    private int mIndex = 0;

    private int mImageArrayIndex = 0;
    private int[][] IMAGE_ARRAY = {
            COUPLE_ANIM_IMAGES,
            SOCCER_ANIM_IMAGES,
            BASE_BALL_IMAGES,
            REDLIGHT_GREENLIGHT_IMAGES
    };

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

    private static final int[] SOCCER_ANIM_IMAGES = {
            R.drawable.soccer1,
            R.drawable.soccer2,
            R.drawable.soccer3,
            R.drawable.soccer4,
            R.drawable.soccer5,
            R.drawable.soccer6,
            R.drawable.soccer7,
            R.drawable.soccer8,
            R.drawable.soccer9,
            R.drawable.soccer10,
            R.drawable.soccer11,
            R.drawable.soccer12,
    };


    private static final int[] BASE_BALL_IMAGES = {
            R.drawable.baseball1,
            R.drawable.baseball2,
            R.drawable.baseball3,
            R.drawable.baseball4,
            R.drawable.baseball5,
            R.drawable.baseball6,
            R.drawable.baseball7,
            R.drawable.baseball8,
            R.drawable.baseball9,
            R.drawable.baseball10,
            R.drawable.baseball11,
            R.drawable.baseball12,
            R.drawable.baseball13,
            R.drawable.baseball14,
    };

    private static final int[] REDLIGHT_GREENLIGHT_IMAGES = {
            R.drawable.red_light_green_light_00001,
            R.drawable.red_light_green_light_00002,
            R.drawable.red_light_green_light_00003,
            R.drawable.red_light_green_light_00004,
            R.drawable.red_light_green_light_00005,
            R.drawable.red_light_green_light_00006,
            R.drawable.red_light_green_light_00007,
            R.drawable.red_light_green_light_00008,
            R.drawable.red_light_green_light_00009,
            R.drawable.red_light_green_light_00010,
            R.drawable.red_light_green_light_00011,
            R.drawable.red_light_green_light_00012,
            R.drawable.red_light_green_light_00013,
            R.drawable.red_light_green_light_00014,
            R.drawable.red_light_green_light_00015,
            R.drawable.red_light_green_light_00016,
            R.drawable.red_light_green_light_00017,
            R.drawable.red_light_green_light_00018,
            R.drawable.red_light_green_light_00019,
            R.drawable.red_light_green_light_00020,
            R.drawable.red_light_green_light_00021,
            R.drawable.red_light_green_light_00022,
            R.drawable.red_light_green_light_00023,
            R.drawable.red_light_green_light_00024,
            R.drawable.red_light_green_light_00025,
            R.drawable.red_light_green_light_00026,
            R.drawable.red_light_green_light_00027,
            R.drawable.red_light_green_light_00028,
            R.drawable.red_light_green_light_00029,
            R.drawable.red_light_green_light_00030,
            R.drawable.red_light_green_light_00031,
            R.drawable.red_light_green_light_00032,
            R.drawable.red_light_green_light_00033,
            R.drawable.red_light_green_light_00034,
            R.drawable.red_light_green_light_00035,
            R.drawable.red_light_green_light_00036,
            R.drawable.red_light_green_light_00037,
            R.drawable.red_light_green_light_00038,
            R.drawable.red_light_green_light_00039,
            R.drawable.red_light_green_light_00040,
            R.drawable.red_light_green_light_00041,
            R.drawable.red_light_green_light_00042,
            R.drawable.red_light_green_light_00043,
            R.drawable.red_light_green_light_00044,
            R.drawable.red_light_green_light_00045,
            R.drawable.red_light_green_light_00046,
            R.drawable.red_light_green_light_00047,
            R.drawable.red_light_green_light_00048,
            R.drawable.red_light_green_light_00049,
            R.drawable.red_light_green_light_00050,
            R.drawable.red_light_green_light_00051,
            R.drawable.red_light_green_light_00052,
            R.drawable.red_light_green_light_00053,
            R.drawable.red_light_green_light_00054,
            R.drawable.red_light_green_light_00055,
            R.drawable.red_light_green_light_00056,
            R.drawable.red_light_green_light_00057,
            R.drawable.red_light_green_light_00058,
            R.drawable.red_light_green_light_00059,
            R.drawable.red_light_green_light_00060,
            R.drawable.red_light_green_light_00061,
            R.drawable.red_light_green_light_00062,
            R.drawable.red_light_green_light_00063,
            R.drawable.red_light_green_light_00064,
            R.drawable.red_light_green_light_00065,
            R.drawable.red_light_green_light_00066,
            R.drawable.red_light_green_light_00067,
            R.drawable.red_light_green_light_00068,
            R.drawable.red_light_green_light_00069,
            R.drawable.red_light_green_light_00070,
            R.drawable.red_light_green_light_00071,
            R.drawable.red_light_green_light_00072,
            R.drawable.red_light_green_light_00073,
            R.drawable.red_light_green_light_00074,
            R.drawable.red_light_green_light_00075,
            R.drawable.red_light_green_light_00076,
            R.drawable.red_light_green_light_00077,
            R.drawable.red_light_green_light_00078,
            R.drawable.red_light_green_light_00079,
            R.drawable.red_light_green_light_00080,
            R.drawable.red_light_green_light_00081,
            R.drawable.red_light_green_light_00082,
            R.drawable.red_light_green_light_00083,
            R.drawable.red_light_green_light_00084,
            R.drawable.red_light_green_light_00085,
            R.drawable.red_light_green_light_00086,
            R.drawable.red_light_green_light_00087,
            R.drawable.red_light_green_light_00088,
            R.drawable.red_light_green_light_00089,
            R.drawable.red_light_green_light_00090,
            R.drawable.red_light_green_light_00091,
            R.drawable.red_light_green_light_00092,
            R.drawable.red_light_green_light_00093,
            R.drawable.red_light_green_light_00094,
            R.drawable.red_light_green_light_00095,
            R.drawable.red_light_green_light_00096,
            R.drawable.red_light_green_light_00097,
            R.drawable.red_light_green_light_00098,
            R.drawable.red_light_green_light_00099,
            R.drawable.red_light_green_light_00100,
            R.drawable.red_light_green_light_00101,
            R.drawable.red_light_green_light_00102,
            R.drawable.red_light_green_light_00103,
            R.drawable.red_light_green_light_00104,
            R.drawable.red_light_green_light_00105,
            R.drawable.red_light_green_light_00106,
            R.drawable.red_light_green_light_00107,
            R.drawable.red_light_green_light_00108,
            R.drawable.red_light_green_light_00109,
            R.drawable.red_light_green_light_00110,
            R.drawable.red_light_green_light_00111,
            R.drawable.red_light_green_light_00112,
            R.drawable.red_light_green_light_00113,
            R.drawable.red_light_green_light_00114,
            R.drawable.red_light_green_light_00115,
            R.drawable.red_light_green_light_00116,
            R.drawable.red_light_green_light_00117,
            R.drawable.red_light_green_light_00118,
            R.drawable.red_light_green_light_00119,
            R.drawable.red_light_green_light_00120,
            R.drawable.red_light_green_light_00121,
            R.drawable.red_light_green_light_00122,
            R.drawable.red_light_green_light_00123,
            R.drawable.red_light_green_light_00124,
            R.drawable.red_light_green_light_00125,
            R.drawable.red_light_green_light_00126,
            R.drawable.red_light_green_light_00127,
            R.drawable.red_light_green_light_00128,
            R.drawable.red_light_green_light_00129,
            R.drawable.red_light_green_light_00130,
            R.drawable.red_light_green_light_00131,
            R.drawable.red_light_green_light_00132,
            R.drawable.red_light_green_light_00133,
            R.drawable.red_light_green_light_00134,
            R.drawable.red_light_green_light_00135,
            R.drawable.red_light_green_light_00136,
            R.drawable.red_light_green_light_00137,
            R.drawable.red_light_green_light_00138,
            R.drawable.red_light_green_light_00139,
            R.drawable.red_light_green_light_00140,
            R.drawable.red_light_green_light_00141,
            R.drawable.red_light_green_light_00142,
            R.drawable.red_light_green_light_00143,
            R.drawable.red_light_green_light_00144,
            R.drawable.red_light_green_light_00145,
            R.drawable.red_light_green_light_00146,
            R.drawable.red_light_green_light_00147,
            R.drawable.red_light_green_light_00148,
            R.drawable.red_light_green_light_00149,
            R.drawable.red_light_green_light_00150,

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_image_anim, null);
        mImageFrame = (FrameLayout) layout.findViewById(R.id.image_container);
        mAnimImage = (ImageView) layout.findViewById(R.id.animation_image);
        mLayout = layout;
        startAnimation();
        mImageArrayIndex = (new Random()).nextInt(IMAGE_ARRAY.length);
        return layout;
    }

    private void startAnimation() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIndex >= IMAGE_ARRAY[mImageArrayIndex].length) {
                    mIndex = 0;
                }
                mAnimImage.setImageResource(IMAGE_ARRAY[mImageArrayIndex][mIndex++]);
                startAnimation();
            }
        }, 166);
    }

    //テンプレートマッチングで返されたピクセルの配置場所を元にフレームを移動させる
    public void moveImageFrameByPixels(int i, int j) {
        mLayout.setPadding(i, j, 0, 0);
    }
}
