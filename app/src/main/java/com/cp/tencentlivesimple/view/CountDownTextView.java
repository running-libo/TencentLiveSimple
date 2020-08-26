package com.cp.tencentlivesimple.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationSet;
import androidx.annotation.NonNull;
import com.cp.tencentlivesimple.util.AnimUtils;

/**
 * create by apple
 * create on 2020/8/26
 * description  倒计时textview
 */
public class CountDownTextView extends androidx.appcompat.widget.AppCompatTextView {
    private int countDownTime = 3;
    private OnCountDownFinishListener listener;

    /** 开始播放计时handler */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (countDownTime>0) {
                //3 2 1
                setVisibility(View.VISIBLE);
                setText(countDownTime + "");
                handler.sendEmptyMessageDelayed(0, 1500);
                playCountDownAnim();
                countDownTime--;
            } else if (countDownTime == 0) {
                // 0
                setText("GO");
                handler.sendEmptyMessageDelayed(0, 1500);
                playCountDownAnim();
                countDownTime--;
            } else {
                //小于0，开播
                setVisibility(View.GONE);
                handler.removeCallbacksAndMessages(null);
                if (listener != null) {
                    listener.onFinish();
                }
            }

        }
    };

    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 开始直播倒计时
     */
    public void startLive() {
        handler.sendEmptyMessage(0);
        if (listener != null) {
            listener.onStartCount();
        }
    }

    /**
     * 播放倒计时动画
     */
    private void playCountDownAnim() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(AnimUtils.scaleAnim(1000, 1.5f, 1, 0));
        animationSet.addAnimation(AnimUtils.alphaAnim(0.5f, 0.9f, 1000, 0));
        startAnimation(animationSet);
    }

    public interface OnCountDownFinishListener {
        void onStartCount();

        void onFinish();
    }

    public void setListener(OnCountDownFinishListener listener) {
        this.listener = listener;
    }
}
