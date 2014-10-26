package android.pictam.sakailab.com.pictam;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.pictam.sakailab.com.pictam.app.PictamApplication;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;

public class TemplateMatch {

    private MatOfByte t_imgs[] = new MatOfByte[4];
    private MatOfByte s_img;

    public TemplateMatch() {
        int[] templateIds = new int[]{
                R.drawable.template10,
//                R.drawable.template20,
                R.drawable.template40,
                R.drawable.template60,
                R.drawable.template80,
        };
        for (int i = 0; i < templateIds.length; i++) {
            Resources res = PictamApplication.getApp().getResources();
            Bitmap tmp = BitmapFactory.decodeResource(res, templateIds[i]);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            tmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            MatOfByte temp = new MatOfByte(bytes);
            t_imgs[i] = temp;
        }
    }

    public void setSearchImg(byte[] search) {
        MatOfByte temp = new MatOfByte(search);
//        MatOfByte dst = new MatOfByte();
//        Imgproc.cvtColor(temp, dst, Imgproc.COLOR_YUV2RGB);
        s_img = temp;
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
        return new int[]{
                rect.x, rect.y
        };
    }

}
