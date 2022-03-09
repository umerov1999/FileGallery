package dev.ragnarok.filegallery.util.existfile;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.List;
import java.util.Locale;

import dev.ragnarok.filegallery.Includes;
import dev.ragnarok.filegallery.model.tags.TagDir;
import dev.ragnarok.filegallery.module.StringExist;
import dev.ragnarok.filegallery.settings.Settings;
import dev.ragnarok.filegallery.util.AppPerms;
import io.reactivex.rxjava3.core.Completable;

public class FileExistNative implements AbsFileExist {
    private final StringExist CachedAudios = new StringExist(true);
    private final StringExist CachedTags = new StringExist(true);

    @Override
    public void addAudio(@NonNull String file) {
        CachedAudios.insert(file.toLowerCase(Locale.getDefault()));
    }

    @Override
    public Completable findAllAudios(Context context) {
        if (!AppPerms.INSTANCE.hasReadWriteStoragePermission(context))
            return Completable.complete();
        return Completable.create(t -> {
            File temp = new File(Settings.INSTANCE.get().main().getMusicDir());
            if (!temp.exists()) {
                t.onComplete();
                return;
            }
            File[] file_list = temp.listFiles();
            if (file_list == null || file_list.length <= 0) {
                t.onComplete();
                return;
            }
            CachedAudios.clear();
            for (File u : file_list) {
                if (u.isFile())
                    CachedAudios.insert(u.getName().toLowerCase(Locale.getDefault()));
            }
        });
    }

    @Override
    public boolean isExistAllAudio(@NonNull String file) {
        String res = file.toLowerCase(Locale.getDefault());
        return CachedAudios.has(res);
    }

    @Override
    public void addTag(@NonNull String path) {
        CachedTags.insert(path);
    }

    @Override
    public void deleteTag(@NonNull String path) {
        CachedTags.delete(path);
    }

    @Override
    public Completable findAllTags() {
        return Completable.create(t -> {
            List<TagDir> list = Includes.getStores().searchQueriesStore().getAllTagDirs().blockingGet();
            CachedAudios.clear();
            CachedTags.clear();
            for (TagDir u : list) {
                CachedTags.insert(u.getPath());
            }
        });
    }

    @Override
    public boolean isExistTag(@NonNull String path) {
        return CachedTags.has(path);
    }
}
