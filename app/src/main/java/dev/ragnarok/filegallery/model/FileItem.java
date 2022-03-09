package dev.ragnarok.filegallery.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import dev.ragnarok.filegallery.media.music.MusicPlaybackController;
import dev.ragnarok.filegallery.module.StringExist;

public class FileItem implements Parcelable {

    public static final Creator<FileItem> CREATOR = new Creator<FileItem>() {
        @Override
        public FileItem createFromParcel(Parcel in) {
            return new FileItem(in);
        }

        @Override
        public FileItem[] newArray(int size) {
            return new FileItem[size];
        }
    };
    private final @FileType
    int type;
    private final String file_name;
    private final String file_path;
    private final String parent_name;
    private final String parent_path;
    private final long modification;
    private final long size;
    private final boolean canRead;
    private boolean isSelected;
    private boolean hasTag;

    public FileItem(@FileType int type, String file_name, String file_path, String parent_name, String parent_path, long modification, long size, boolean canRead) {
        this.type = type;
        this.file_name = file_name;
        this.file_path = file_path;
        this.parent_name = parent_name;
        this.parent_path = parent_path;
        this.size = size;
        this.canRead = canRead;
        this.modification = modification;
    }

    protected FileItem(Parcel in) {
        type = in.readInt();
        file_name = in.readString();
        file_path = in.readString();
        parent_name = in.readString();
        parent_path = in.readString();
        modification = in.readLong();
        size = in.readLong();
        canRead = in.readByte() != 0;
        isSelected = in.readByte() != 0;
        hasTag = in.readByte() != 0;
    }

    public boolean isHasTag() {
        return hasTag;
    }

    public FileItem checkTag() {
        hasTag = MusicPlaybackController.tracksExist.isExistTag(file_path);
        return this;
    }

    public int getType() {
        return type;
    }

    public String getFile_name() {
        return file_name;
    }

    public int getFileNameHash() {
        return StringExist.calculateCRC32(file_name);
    }

    public int getFilePathHash() {
        return StringExist.calculateCRC32(file_path);
    }

    public String getFile_path() {
        return file_path;
    }

    public String getParent_name() {
        return parent_name;
    }

    public String getParent_path() {
        return parent_path;
    }

    public long getModification() {
        return modification;
    }

    public long getSize() {
        return size;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public FileItem setSelected(boolean selected) {
        isSelected = selected;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return file_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(file_name);
        dest.writeString(file_path);
        dest.writeString(parent_name);
        dest.writeString(parent_path);
        dest.writeLong(modification);
        dest.writeLong(size);
        dest.writeByte((byte) (canRead ? 1 : 0));
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (hasTag ? 1 : 0));
    }
}
