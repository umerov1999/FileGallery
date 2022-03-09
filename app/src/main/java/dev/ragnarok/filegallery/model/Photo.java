package dev.ragnarok.filegallery.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import dev.ragnarok.filegallery.module.parcel.ParcelNative;


@Keep
public class Photo implements Parcelable, ParcelNative.ParcelableNative {

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public static final ParcelNative.Creator<Photo> NativeCreator = Photo::new;
    private int id;
    private int ownerId;
    private String photo_url;
    private String preview_url;
    private String text;
    private long date;
    private boolean isGif;
    private boolean isDownload;

    public Photo() {

    }

    protected Photo(ParcelNative in) {
        id = in.readInt();
        ownerId = in.readInt();
        photo_url = in.readString();
        preview_url = in.readString();
        text = in.readString();
        date = in.readLong();
        isDownload = in.readBoolean();
        isGif = in.readBoolean();
    }

    protected Photo(Parcel in) {
        id = in.readInt();
        ownerId = in.readInt();
        photo_url = in.readString();
        preview_url = in.readString();
        text = in.readString();
        date = in.readLong();
        isDownload = in.readByte() != 0;
        isGif = in.readByte() != 0;
    }

    public boolean isGif() {
        return isGif;
    }

    public Photo setGif(boolean gif) {
        isGif = gif;
        return this;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public Photo setDownload(boolean download) {
        isDownload = download;
        return this;
    }

    public int getId() {
        return id;
    }

    public Photo setId(int id) {
        this.id = id;
        return this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public Photo setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getText() {
        return text;
    }

    public Photo setText(String text) {
        this.text = text;
        return this;
    }

    public long getDate() {
        return date;
    }

    public Photo setDate(long date) {
        this.date = date;
        return this;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public Photo setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
        return this;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public Photo setPreview_url(String preview_url) {
        this.preview_url = preview_url;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(ownerId);
        parcel.writeString(photo_url);
        parcel.writeString(preview_url);
        parcel.writeString(text);
        parcel.writeLong(date);
        parcel.writeByte((byte) (isDownload ? 1 : 0));
        parcel.writeByte((byte) (isGif ? 1 : 0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;
        return id == photo.id && ownerId == photo.ownerId;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + ownerId;
        return result;
    }

    @Override
    public void writeToParcelNative(ParcelNative parcel) {
        parcel.writeInt(id);
        parcel.writeInt(ownerId);
        parcel.writeString(photo_url);
        parcel.writeString(preview_url);
        parcel.writeString(text);
        parcel.writeLong(date);
        parcel.writeBoolean(isDownload);
        parcel.writeBoolean(isGif);
    }
}
