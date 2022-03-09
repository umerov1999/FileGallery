package dev.ragnarok.filegallery.model;

import android.os.Parcel;
import android.os.Parcelable;

import dev.ragnarok.filegallery.module.parcel.ParcelNative;


public class Video implements Parcelable, ParcelNative.ParcelableNative {

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
    public static final ParcelNative.Creator<Video> NativeCreator = Video::new;
    private int id;
    private int ownerId;
    private String title;
    private String description;
    private String link;
    private long date;
    private String image;
    private boolean repeat;
    private int duration;

    public Video() {

    }

    protected Video(Parcel in) {
        id = in.readInt();
        ownerId = in.readInt();
        title = in.readString();
        description = in.readString();
        link = in.readString();
        date = in.readLong();
        image = in.readString();
        repeat = in.readByte() != 0;
        duration = in.readInt();
    }

    protected Video(ParcelNative in) {
        id = in.readInt();
        ownerId = in.readInt();
        title = in.readString();
        description = in.readString();
        link = in.readString();
        date = in.readLong();
        image = in.readString();
        repeat = in.readBoolean();
        duration = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(ownerId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeLong(date);
        dest.writeString(image);
        dest.writeByte((byte) (repeat ? 1 : 0));
        dest.writeInt(duration);
    }

    @Override
    public void writeToParcelNative(ParcelNative dest) {
        dest.writeInt(id);
        dest.writeInt(ownerId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeLong(date);
        dest.writeString(image);
        dest.writeBoolean(repeat);
        dest.writeInt(duration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public Video setId(int id) {
        this.id = id;
        return this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public Video setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Video setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Video setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Video setLink(String link) {
        this.link = link;
        return this;
    }

    public long getDate() {
        return date;
    }

    public Video setDate(long date) {
        this.date = date;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Video setImage(String image) {
        this.image = image;
        return this;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public Video setRepeat(boolean repeat) {
        this.repeat = repeat;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Video setDuration(int duration) {
        this.duration = duration;
        return this;
    }
}
