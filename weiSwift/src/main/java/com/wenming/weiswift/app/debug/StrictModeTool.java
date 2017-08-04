package com.wenming.weiswift.app.debug;

import android.os.StrictMode;

/**
 * 用来检测程序中违例情况的开发者工具
 * <p/>
 * Created by wenming on 2017/8/4.
 */
public class StrictModeTool {

    public static void enableDefaults() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }
}
