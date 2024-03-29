package com.cp.tencentlivesimple.model.impl.av.liveplayer;

import android.content.Context;
import com.cp.tencentlivesimple.model.impl.base.TXCallback;
import com.tencent.rtmp.ui.TXCloudVideoView;

public interface ITXLivePlayerRoom {
    void init(Context context);

    void startPlay(String playURL, TXCloudVideoView view, TXCallback callback);

    void stopPlay(String playURL, TXCallback callback);

    void stopAllPlay();

    void muteRemoteAudio(String playURL, boolean mute);

    void muteAllRemoteAudio(boolean mute);

    void showVideoDebugLog(boolean isShow);
}
