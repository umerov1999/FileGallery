package dev.ragnarok.filegallery.model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({FileType.error,
        FileType.folder,
        FileType.photo,
        FileType.video,
        FileType.audio})
@Retention(RetentionPolicy.SOURCE)
public @interface FileType {
    int error = -1;
    int folder = 0;
    int photo = 1;
    int video = 2;
    int audio = 3;
}

