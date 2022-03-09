package dev.ragnarok.filegallery.media.video;

import android.content.Context;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import dev.ragnarok.filegallery.model.VideoSize;


public interface IVideoPlayer {
    void updateSource(Context context, String url);

    void play();

    void pause();

    void release();

    long getDuration();

    long getCurrentPosition();

    void seekTo(long position);

    boolean isPlaying();

    int getBufferPercentage();

    long getBufferPosition();

    void setSurfaceHolder(SurfaceHolder holder);

    boolean isPlaybackSpeed();

    void togglePlaybackSpeed();

    void addVideoSizeChangeListener(IVideoSizeChangeListener listener);

    void removeVideoSizeChangeListener(IVideoSizeChangeListener listener);

    interface IVideoSizeChangeListener {
        void onVideoSizeChanged(@NonNull IVideoPlayer player, VideoSize size);
    }

    interface IUpdatePlayListener {
        void onPlayChanged(boolean isPause);
    }
}