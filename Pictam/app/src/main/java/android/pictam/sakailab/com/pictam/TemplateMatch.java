package android.pictam.sakailab.com.pictam;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.pictam.sakailab.com.pictam.app.PictamApplication;
import android.pictam.sakailab.com.pictam.config.Config;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class TemplateMatch {

    private static Mat sTempImgs[] = new Mat[5];
    private Mat mSearchImg = new Mat();

    public TemplateMatch() {
        int[] templateIds = new int[]{
                R.drawable.template10,
                R.drawable.template20,
                R.drawable.template40,
                R.drawable.template60,
                R.drawable.template80,
        };
        for (int i = 0; i < templateIds.length; i++) {
            sTempImgs[i] = new Mat();
            Resources res = PictamApplication.getApp().getResources();
            Bitmap tmp = BitmapFactory.decodeResource(res, templateIds[i]);
            Utils.bitmapToMat(tmp, sTempImgs[i]);
        }
    }

    //探査対象となるカメラのプレビュー画像をセットする
    public void setSearchImg(Mat data) {
        mSearchImg = data;
    }

    public int[] match() {
        Mat result_img = new Mat();
        double maxVal = 0.0;
        int max = 0;
        Point max_pt = new Point();
        for (int i = 0; i < sTempImgs.length; i++) {
            double maxVal_tmp;
            Point max_pt_tmp;
            Imgproc.matchTemplate(mSearchImg, sTempImgs[i], result_img, Imgproc.TM_CCOEFF_NORMED);

            Core.MinMaxLocResult mm = Core.minMaxLoc(result_img);
            maxVal_tmp = mm.maxVal;
            max_pt_tmp = mm.maxLoc;
            if (maxVal_tmp != 0.0) {
                if (maxVal < maxVal_tmp) {
                    max = i;
                    maxVal = maxVal_tmp;
                    max_pt = max_pt_tmp;
                }
            } else {
                max = i;
                maxVal = maxVal_tmp;
                max_pt = max_pt_tmp;
            }
        }
        Rect rect = new Rect(0, 0, sTempImgs[max].cols(), sTempImgs[max].rows());
        rect.x = (int) max_pt.x;
        rect.y = (int) max_pt.y;

        Log.d(Config.DEBUG_TAG, "s_width:" + mSearchImg.width() + " s_height:" + mSearchImg.height());
        Log.d(Config.DEBUG_TAG, "t_width:" + sTempImgs[0].width() + " t_height:" + sTempImgs[0].height());
        Log.d(Config.DEBUG_TAG, "r_width:" + result_img.width() + " r_height:" + result_img.height());
        return new int[]{
                rect.x, rect.y
        };
    }


}
