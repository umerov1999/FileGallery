package dev.ragnarok.filegallery.model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({SectionItem.NULL,
        SectionItem.FILE_MANAGER,
        SectionItem.SETTINGS,
        SectionItem.LOCAL_SERVER,
        SectionItem.TAGS})
@Retention(RetentionPolicy.SOURCE)
public @interface SectionItem {
    int NULL = -1;
    int FILE_MANAGER = 0;
    int SETTINGS = 1;
    int LOCAL_SERVER = 2;
    int TAGS = 3;
}

