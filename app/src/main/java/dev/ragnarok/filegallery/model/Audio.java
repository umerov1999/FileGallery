package dev.ragnarok.filegallery.model;

import static dev.ragnarok.filegallery.util.Utils.stringEmptyIfNull;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import dev.ragnarok.filegallery.util.DownloadWorkUtils;

@Keep
public class Audio implements Parcelable {

    public static final Creator<Audio> CREATOR = new Creator<Audio>() {
        @Override
        public Audio createFromParcel(Parcel in) {
            return new Audio(in);
        }

        @Override
        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };
    private int id;
    private int ownerId;
    private String thumb_image;
    private String artist;
    private String title;
    private int duration;
    private String url;
    private boolean animationNow;
    private boolean isSelected;
    private boolean is_local;
    private boolean is_localServer;
    private boolean downloadIndicator;

    @SuppressWarnings("unused")
    public Audio() {

    }

    protected Audio(Parcel in) {
        id = in.readInt();
        ownerId = in.readInt();
        artist = in.readString();
        title = in.readString();
        duration = in.readInt();
        url = in.readString();
        animationNow = in.readByte() != 0;
        isSelected = in.readByte() != 0;
        is_local = in.readByte() != 0;
        is_localServer = in.readByte() != 0;
        downloadIndicator = in.readByte() != 0;
        thumb_image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(ownerId);
        dest.writeString(artist);
        dest.writeString(title);
        dest.writeInt(duration);
        dest.writeString(url);
        dest.writeByte((byte) (animationNow ? 1 : 0));
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (is_local ? 1 : 0));
        dest.writeByte((byte) (is_localServer ? 1 : 0));
        dest.writeByte((byte) (downloadIndicator ? 1 : 0));
        dest.writeString(thumb_image);
    }

    public boolean getDownloadIndicator() {
        return downloadIndicator;
    }

    public void setDownloadIndicator(boolean state) {
        downloadIndicator = state;
    }

    public Audio updateDownloadIndicator() {
        downloadIndicator = DownloadWorkUtils.TrackIsDownloaded(this);
        return this;
    }

    public boolean isAnimationNow() {
        return animationNow;
    }

    public void setAnimationNow(boolean animationNow) {
        this.animationNow = animationNow;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public Audio setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
        return this;
    }

    public int getId() {
        return id;
    }

    public Audio setId(int id) {
        this.id = id;
        return this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public Audio setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public Audio setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Audio setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Audio setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Audio setUrl(String url) {
        this.url = url;
        return this;
    }

    public boolean isLocal() {
        return is_local;
    }

    public Audio setIsLocal() {
        is_local = true;
        return this;
    }

    public boolean isLocalServer() {
        return is_localServer;
    }

    public Audio setIsLocalServer() {
        is_localServer = true;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getArtistAndTitle() {
        return stringEmptyIfNull(artist) + " - " + stringEmptyIfNull(title);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Audio))
            return false;

        Audio audio = (Audio) o;
        return id == audio.id && ownerId == audio.ownerId;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + ownerId;
        return result;
    }
}
