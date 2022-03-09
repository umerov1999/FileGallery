package dev.ragnarok.filegallery.util.existfile;

import android.content.Context;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.core.Completable;

public interface AbsFileExist {

    void addAudio(@NonNull String file);

    Completable findAllAudios(Context context);

    boolean isExistAllAudio(@NonNull String file);

    void addTag(@NonNull String path);

    void deleteTag(@NonNull String path);

    Completable findAllTags();

    boolean isExistTag(@NonNull String path);
}
