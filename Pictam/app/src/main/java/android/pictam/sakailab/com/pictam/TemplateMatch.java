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
    private int mBestTempIndex;
    private Point mBestMatchPoint = new Point();
    private boolean mIsBestMatch = false;
    private static final double threshold = 0.6;

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
        Mat search_img;
        double maxVal = 0.0;
        int max = 0;
        Point max_pt = new Point();

        // get half of mSearchImg
        //前回の探索が成功している場合、マッチした付近を成功したテンプレートで探索
        if (mIsBestMatch) {
            double offsetX = mBestMatchPoint.x - sTempImgs[mBestTempIndex].cols();
            double offsetY = mBestMatchPoint.y - sTempImgs[mBestTempIndex].rows();
            search_img = cropMat(mSearchImg, new Rect((int) offsetX, (int) offsetY, sTempImgs[mBestTempIndex].rows() * 3, sTempImgs[mBestTempIndex].rows() * 3));
            Imgproc.matchTemplate(search_img, sTempImgs[mBestTempIndex], result_img, Imgproc.TM_CCOEFF_NORMED);
            Core.MinMaxLocResult mm = Core.minMaxLoc(result_img);
            if (mm.maxVal < threshold) {
                mIsBestMatch = false;
            } else {
                max_pt = mm.maxLoc;
                maxVal = mm.maxVal;
                max = mBestTempIndex;
            }
        }

        //失敗した場合、通常の探索へ（プレビューの上半分のみを探索）
        if (!mIsBestMatch) {
            search_img = cropMat(mSearchImg, new Rect(0, 0, mSearchImg.cols(), mSearchImg.rows() / 2));
            for (int i = 0; i < sTempImgs.length; i++) {
                double maxVal_tmp;
                Point max_pt_tmp;
                Imgproc.matchTemplate(search_img, sTempImgs[i], result_img, Imgproc.TM_CCOEFF_NORMED);
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
            //しきい値をつければ信号が変わった時を検知できる
            if (maxVal < threshold) {
                return new int[]{
                        -1, -1
                };
            } else {
                mIsBestMatch = true;
            }
        }

        Rect rect = new Rect(0, 0, sTempImgs[max].cols(), sTempImgs[max].rows());
        mBestMatchPoint = max_pt;
        mBestTempIndex = max;
        rect.x = (int) max_pt.x;
        rect.y = (int) max_pt.y;

        Log.d(Config.DEBUG_TAG, "s_width:" + mSearchImg.width() + " s_height:" + mSearchImg.height());
        Log.d(Config.DEBUG_TAG, "t_width:" + sTempImgs[0].width() + " t_height:" + sTempImgs[0].height());
        Log.d(Config.DEBUG_TAG, "r_width:" + result_img.width() + " r_height:" + result_img.height());

        return new int[]{
                rect.x, rect.y
        };
    }

    private Mat cropMat(Mat mSearchImg, Rect crip) {
        return new Mat(mSearchImg, crip);
    }

}
