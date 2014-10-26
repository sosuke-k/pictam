package android.pictam.sakailab.com.pictam.app;

import android.app.Application;

/**
 * Created by taisho6339 on 2014/10/17.
 */
public class PictamApplication extends Application {
    //    static {
//        System.load();
//    }
    private static PictamApplication APP_INSTANCE;

    public PictamApplication() {
        APP_INSTANCE = this;
    }

    public static PictamApplication getApp() {
        return APP_INSTANCE;
    }
}
