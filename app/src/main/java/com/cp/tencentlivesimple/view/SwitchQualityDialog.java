package com.cp.tencentlivesimple.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.cp.tencentlivesimple.R;

/**
 * create by apple
 * create on 2020/8/26
 * description
 */
public class SwitchQualityDialog extends Dialog implements View.OnClickListener {
    private TextView tvStandard, tvHD, tvSuperClear;
    private OnChoosedListener onChoosedListener;

    public SwitchQualityDialog(@NonNull Context context, int themeResId, OnChoosedListener onChoosedListener) {
        super(context, themeResId);
        setContentView(R.layout.dialog_switch_quality);
        this.onChoosedListener = onChoosedListener;

        initView();

        show();
    }

    private void initView() {
        tvStandard = findViewById(R.id.tvFluent);
        tvHD = findViewById(R.id.tvHD);
        tvSuperClear = findViewById(R.id.tvSuperClear);

        tvStandard.setOnClickListener(this);
        tvHD.setOnClickListener(this);
        tvSuperClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFluent:
                onChoosedListener.onChoosed(0);
                break;
            case R.id.tvHD:
                onChoosedListener.onChoosed(1);
                break;
            case R.id.tvSuperClear:
                onChoosedListener.onChoosed(2);
                break;

        }
        dismiss();
    }

    public interface OnChoosedListener {
        void onChoosed(int pos);
    }
}
