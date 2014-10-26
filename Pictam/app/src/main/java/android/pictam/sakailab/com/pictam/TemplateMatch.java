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
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class TemplateMatch {

    private Mat t_imgs[] = new Mat[5];
    private static Mat s_img = new Mat();

    static {
        Resources res = PictamApplication.getApp().getResources();
        Bitmap tmp = BitmapFactory.decodeResource(res, R.drawable.sample);
        Mat mat = new Mat();
        Utils.bitmapToMat(tmp, s_img);
    }

    public TemplateMatch() {
        int[] templateIds = new int[]{
                R.drawable.template10,
                R.drawable.template20,
                R.drawable.template40,
                R.drawable.template60,
                R.drawable.template80,
        };
        for (int i = 0; i < templateIds.length; i++) {
            t_imgs[i] = new Mat();
            Resources res = PictamApplication.getApp().getResources();
            Bitmap tmp = BitmapFactory.decodeResource(res, templateIds[i]);
            Utils.bitmapToMat(tmp, t_imgs[i]);
        }
    }

    public void setSearchImg(byte[] search) {
//        ArrayList<Byte> rgb = new ArrayList<Byte>();
//        for (int i = 0; i < search.length; i += 3)
//            yuvToRgb(search[i], search[i + 1], search[i + 2], rgb);
//        Byte[] temp = new Byte[search.length];
//        temp = rgb.toArray(temp);
//        search = wrapperToPrimitive(temp);
    }


    public int[] match() {
        Mat result_img = new Mat();
        double maxVal = 0.0;
        int max = 0;
        Point max_pt = new Point();
        for (int i = 0; i < t_imgs.length; i++) {
            double maxVal_tmp;
            Point max_pt_tmp;
            Imgproc.matchTemplate(s_img, t_imgs[i], result_img, Imgproc.TM_CCOEFF_NORMED);

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
        Rect rect = new Rect(0, 0, t_imgs[max].cols(), t_imgs[max].rows());
        rect.x = (int) max_pt.x;
        rect.y = (int) max_pt.y;
//        Log.d(Config.DEBUG_TAG, "width:" + s_img.size().width + " height:" + s_img.size().height);
        Log.d(Config.DEBUG_TAG, "s_width:" + s_img.width() + " s_height:" + s_img.height());
        Log.d(Config.DEBUG_TAG, "t_width:" + t_imgs[0].width() + " t_height:" + t_imgs[0].height());
        Log.d(Config.DEBUG_TAG, "r_width:" + result_img.width() + " r_height:" + result_img.height());
//        Core.rectangle(result_img, max_pt, new Point(max_pt.x + t_imgs[3].width(), max_pt.y + t_imgs[3].height()), new Scalar(255, 0, 0), 2);
        return new int[]{
                rect.x, rect.y
        };
    }


    private void yuvToRgb(byte yValue, byte uValue, byte vValue, ArrayList<Byte> rgb) {
        int y = (yValue) & (0x00FF);
        int u = (uValue) & (0xFF);
        int v = (vValue) & (0xFF);
        byte r = (byte) (y + (1.370705f * (v - 128)));
        byte g = (byte) (y - (0.698001f * (v - 128)) - (0.337633f * (u - 128)));
        byte b = (byte) (y + (1.732446f * (u - 128)));
        rgb.add(r);
        rgb.add(g);
        rgb.add(b);
    }

    private byte[] wrapperToPrimitive(Byte[] b) {
        byte[] goal = new byte[b.length];
        for (int i = 0; i < b.length; i++) {
            goal[i] = b[i];
        }
        return goal;
    }


}
