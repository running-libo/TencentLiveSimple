package com.cp.tencentlivesimple;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;

/**
 * create by libo
 * create on 2019/10/29
 * description 确定公用弹框
 */
public class ConfirmDialog extends Dialog {
    TextView tvDialogTitle;
    TextView tvContent;
    TextView tvSureadd;
    TextView tvCanceladd;
    private Context context;

    public ConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_confirm);

        tvDialogTitle = findViewById(R.id.tv_dialog_title);
        tvSureadd = findViewById(R.id.tv_sureadd);
        tvCanceladd = findViewById(R.id.tv_canceladd);
        tvContent = findViewById(R.id.tv_content);

        initSize();

        show();

        tvSureadd.setOnClickListener(v -> {
            if (onClickListener!= null) {
                onClickListener.onConfirm();
                dismiss();
            }
        });

        tvCanceladd.setOnClickListener(v -> {
            if (onClickListener!= null) {
                onClickListener.onCancel();
                dismiss();
            }
            dismiss();
        });
    }

    /**
     * 设置dialog的宽度铺满屏幕
     */
    private void initSize() {
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    /**
     * 设置标题
     * @param title
     * @return
     */
    public ConfirmDialog setTitle(String title) {
        tvDialogTitle.setText(title);
        return this;
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static class OnClickListener{
        public void onCancel(){}
        public void onConfirm(){}
    }
}
