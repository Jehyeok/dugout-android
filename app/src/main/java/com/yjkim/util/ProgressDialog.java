package com.yjkim.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.WindowManager;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.yjkim.dugout.R;

/**
 * Created by jehyeok on 2/6/15.
 */
public class ProgressDialog extends Dialog {
    private ProgressWheel wheel;

    public ProgressDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_progress);
        ProgressWheel wheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        wheel.setRimColor(Color.TRANSPARENT);
        wheel.setBarColor(Color.rgb(61, 174, 213));
        wheel.setLinearProgress(true);
        wheel.setSpinSpeed(1);
        wheel.spin();
    }
}