package dev.ragnarok.filegallery.model.tags;

import android.os.Parcel;
import android.os.Parcelable;

import dev.ragnarok.filegallery.model.FileType;
import dev.ragnarok.filegallery.module.StringExist;

public class TagDir implements Parcelable {

    public static final Creator<TagDir> CREATOR = new Creator<TagDir>() {
        @Override
        public TagDir createFromParcel(Parcel in) {
            return new TagDir(in);
        }

        @Override
        public TagDir[] newArray(int size) {
            return new TagDir[size];
        }
    };

    private int id;
    private int owner_id;
    private long size;
    private String name;
    private String path;
    private @FileType
    int type = FileType.folder;
    private boolean isSelected;

    public TagDir() {

    }

    protected TagDir(Parcel in) {
        id = in.readInt();
        owner_id = in.readInt();
        name = in.readString();
        path = in.readString();
        type = in.readInt();
        size = in.readLong();
        isSelected = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeInt(owner_id);
        parcel.writeString(name);
        parcel.writeString(path);
        parcel.writeInt(type);
        parcel.writeLong(size);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }

    public boolean isSelected() {
        return isSelected;
    }

    public TagDir setSelected(boolean selected) {
        isSelected = selected;
        return this;
    }

    public int getFileNameHash() {
        return StringExist.calculateCRC32(name);
    }

    public int getFilePathHash() {
        return StringExist.calculateCRC32(path);
    }

    public int getId() {
        return id;
    }

    public TagDir setId(int id) {
        this.id = id;
        return this;
    }

    public @FileType
    int getType() {
        return type;
    }

    public TagDir setType(@FileType int type) {
        this.type = type;
        return this;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public TagDir setOwner_id(int owner_id) {
        this.owner_id = owner_id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TagDir setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public TagDir setPath(String path) {
        this.path = path;
        return this;
    }

    public long getSize() {
        return size;
    }

    public TagDir setSize(long size) {
        this.size = size;
        return this;
    }
}