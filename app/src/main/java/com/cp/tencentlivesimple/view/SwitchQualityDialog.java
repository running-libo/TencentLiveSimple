package com.cp.tencentlivesimple.view;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;

import com.cp.tencentlivesimple.R;

/**
 * create by apple
 * create on 2020/8/26
 * description
 */
public class SwitchQualityDialog extends Dialog {

    public SwitchQualityDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_switch_quality);

        show();
    }
}
