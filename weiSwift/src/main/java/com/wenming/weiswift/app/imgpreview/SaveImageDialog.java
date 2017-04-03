package com.wenming.weiswift.app.imgpreview;

import android.content.Context;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.ViewGroup;

/**
 * Created by wenmingvs on 16/9/2.
 */
public class SaveImageDialog {

    public static void showDialog(String url, Context context) {
        ImageDetailDialog dialog = new ImageDetailDialog(url, context);
        if (dialog.isShowing()) {
            dialog.dismiss();
        } else {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(50);
            dialog.show();
            dialog.getWindow().setLayout((ViewGroup.LayoutParams.MATCH_PARENT), (ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
    }
}
