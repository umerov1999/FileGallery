package dev.ragnarok.filegallery.media.video;

import static dev.ragnarok.filegallery.util.Objects.nonNull;

import android.content.Context;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import dev.ragnarok.filegallery.Constants;
import dev.ragnarok.filegallery.media.exo.ExoUtil;
import dev.ragnarok.filegallery.model.VideoSize;
import dev.ragnarok.filegallery.util.Utils;

public class ExoVideoPlayer implements IVideoPlayer {

    private final ExoPlayer player;
    private final OnVideoSizeChangedListener onVideoSizeChangedListener = new OnVideoSizeChangedListener(this);
    private final List<IVideoSizeChangeListener> videoSizeChangeListeners = new ArrayList<>(1);
    private MediaSource source;
    private boolean supposedToBePlaying;
    private boolean prepareCalled;
    private boolean playbackSpeed;

    public ExoVideoPlayer(Context context, String url, @NonNull IUpdatePlayListener playListener) {
        player = createPlayer(context);
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_ENDED) {
                    player.seekTo(0);
                    pause();
                    playListener.onPlayChanged(true);
                }
            }
        });
        player.addListener(onVideoSizeChangedListener);
        source = createMediaSource(context, url);
    }

    private static MediaSource createMediaSource(Context context, String url) {
        String userAgent = Constants.USER_AGENT;
        if (url.contains("file://") || url.contains("content://")) {
            return new ProgressiveMediaSource.Factory(new DefaultDataSource.Factory(context)).createMediaSource(Utils.makeMediaItem(url));
        }
        return new ProgressiveMediaSource.Factory(Utils.getExoPlayerFactory(userAgent)).createMediaSource(Utils.makeMediaItem(url));
    }

    @Override
    public void updateSource(Context context, String url) {
        source = createMediaSource(context, url);
        player.setMediaSource(source);
        player.setPlaybackSpeed(playbackSpeed ? 2f : 1f);
        player.prepare();
        ExoUtil.startPlayer(player);
    }

    private ExoPlayer createPlayer(Context context) {
        ExoPlayer ret = new ExoPlayer.Builder(context, new DefaultRenderersFactory(context)).build();
        ret.setAudioAttributes(new AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MOVIE).setUsage(C.USAGE_MEDIA).build(), true);
        return ret;
    }

    @Override
    public void play() {
        if (supposedToBePlaying) {
            return;
        }

        supposedToBePlaying = true;

        if (!prepareCalled) {
            player.setMediaSource(source);
            player.setPlaybackSpeed(playbackSpeed ? 2f : 1f);
            player.prepare();
            prepareCalled = true;
        }

        ExoUtil.startPlayer(player);
    }

    @Override
    public void pause() {
        if (!supposedToBePlaying) {
            return;
        }

        supposedToBePlaying = false;
        ExoUtil.pausePlayer(player);
    }

    @Override
    public boolean isPlaybackSpeed() {
        return playbackSpeed;
    }

    @Override
    public void togglePlaybackSpeed() {
        if (nonNull(player)) {
            playbackSpeed = !playbackSpeed;
            player.setPlaybackSpeed(playbackSpeed ? 2f : 1f);
        }
    }

    @Override
    public void release() {
        if (nonNull(player)) {
            try {
                player.removeListener(onVideoSizeChangedListener);
                player.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public long getDuration() {
        return player.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(long position) {
        player.seekTo(position);
    }

    @Override
    public boolean isPlaying() {
        return supposedToBePlaying;
    }

    @Override
    public int getBufferPercentage() {
        return player.getBufferedPercentage();
    }

    @Override
    public long getBufferPosition() {
        return player.getBufferedPosition();
    }

    @Override
    public void setSurfaceHolder(SurfaceHolder holder) {
        player.setVideoSurfaceHolder(holder);
    }

    private void onVideoSizeChanged(int w, int h) {
        for (IVideoSizeChangeListener listener : videoSizeChangeListeners) {
            listener.onVideoSizeChanged(this, new VideoSize(w, h));
        }
    }

    @Override
    public void addVideoSizeChangeListener(IVideoSizeChangeListener listener) {
        videoSizeChangeListeners.add(listener);
    }

    @Override
    public void removeVideoSizeChangeListener(IVideoSizeChangeListener listener) {
        videoSizeChangeListeners.remove(listener);
    }

    private static final class OnVideoSizeChangedListener implements Player.Listener {

        final WeakReference<ExoVideoPlayer> ref;

        private OnVideoSizeChangedListener(ExoVideoPlayer player) {
            ref = new WeakReference<>(player);
        }

        @Override
        public void onVideoSizeChanged(@NonNull com.google.android.exoplayer2.video.VideoSize size) {
            ExoVideoPlayer player = ref.get();
            if (player != null) {
                player.onVideoSizeChanged(size.width, size.height);
            }
        }

        @Override
        public void onRenderedFirstFrame() {

        }
    }
}