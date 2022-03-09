package dev.ragnarok.filegallery.model.tags;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import dev.ragnarok.filegallery.model.FileType;

public class TagFull implements Parcelable {

    public static final Creator<TagFull> CREATOR = new Creator<TagFull>() {
        @Override
        public TagFull createFromParcel(Parcel in) {
            return new TagFull(in);
        }

        @Override
        public TagFull[] newArray(int size) {
            return new TagFull[size];
        }
    };

    private String name;
    private List<TagDirEntry> dirs;

    public TagFull() {

    }

    protected TagFull(Parcel in) {
        name = in.readString();
        dirs = in.createTypedArrayList(TagDirEntry.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeTypedList(dirs);
    }

    public List<TagDirEntry> getDirs() {
        return dirs;
    }

    public TagFull setDirs(List<TagDirEntry> dirs) {
        this.dirs = dirs;
        return this;
    }

    public String getName() {
        return name;
    }

    public TagFull setName(String name) {
        this.name = name;
        return this;
    }

    public static class TagDirEntry implements Parcelable {

        public static final Creator<TagDirEntry> CREATOR = new Creator<TagDirEntry>() {
            @Override
            public TagDirEntry createFromParcel(Parcel in) {
                return new TagDirEntry(in);
            }

            @Override
            public TagDirEntry[] newArray(int size) {
                return new TagDirEntry[size];
            }
        };

        private String name;
        private String path;
        private @FileType
        int type = FileType.folder;

        public TagDirEntry() {

        }

        protected TagDirEntry(Parcel in) {
            name = in.readString();
            path = in.readString();
            type = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeString(name);
            parcel.writeString(path);
            parcel.writeInt(type);
        }

        public @FileType
        int getType() {
            return type;
        }

        public TagDirEntry setType(@FileType int type) {
            this.type = type;
            return this;
        }

        public String getName() {
            return name;
        }

        public TagDirEntry setName(String name) {
            this.name = name;
            return this;
        }

        public String getPath() {
            return path;
        }

        public TagDirEntry setPath(String path) {
            this.path = path;
            return this;
        }
    }
} 
