package dev.ragnarok.filegallery.model.tags;

import android.os.Parcel;
import android.os.Parcelable;

public class TagOwner implements Parcelable {

    public static final Creator<TagOwner> CREATOR = new Creator<TagOwner>() {
        @Override
        public TagOwner createFromParcel(Parcel in) {
            return new TagOwner(in);
        }

        @Override
        public TagOwner[] newArray(int size) {
            return new TagOwner[size];
        }
    };

    private int id;
    private String name;
    private int count;

    public TagOwner() {

    }

    protected TagOwner(Parcel in) {
        id = in.readInt();
        name = in.readString();
        count = in.readByte();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(count);
    }

    public int getCount() {
        return count;
    }

    public TagOwner setCount(int count) {
        this.count = count;
        return this;
    }

    public int getId() {
        return id;
    }

    public TagOwner setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TagOwner setName(String name) {
        this.name = name;
        return this;
    }
}