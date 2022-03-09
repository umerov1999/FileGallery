package dev.ragnarok.filegallery.util.existfile;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import dev.ragnarok.filegallery.Includes;
import dev.ragnarok.filegallery.model.tags.TagDir;
import dev.ragnarok.filegallery.settings.Settings;
import dev.ragnarok.filegallery.util.AppPerms;
import dev.ragnarok.filegallery.util.Utils;
import io.reactivex.rxjava3.core.Completable;

public class FileExistJVM implements AbsFileExist {
    private final List<String> CachedAudios = new LinkedList<>();
    private final List<String> CachedTags = new LinkedList<>();
    private final Object isBusyLock = new Object();
    private boolean isBusy;

    private boolean setBusy(boolean nBusy) {
        synchronized (isBusyLock) {
            if (isBusy && nBusy) {
                return false;
            }
            isBusy = nBusy;
        }
        return true;
    }

    @Override
    public void addAudio(@NonNull String file) {
        if (!setBusy(true)) {
            return;
        }
        CachedAudios.add(file.toLowerCase(Locale.getDefault()));
        setBusy(false);
    }

    @Override
    public Completable findAllAudios(Context context) {
        if (!AppPerms.INSTANCE.hasReadWriteStoragePermission(context))
            return Completable.complete();
        return Completable.create(t -> {
            if (!setBusy(true)) {
                return;
            }
            File temp = new File(Settings.INSTANCE.get().main().getMusicDir());
            if (!temp.exists()) {
                setBusy(false);
                t.onComplete();
                return;
            }
            File[] file_list = temp.listFiles();
            if (file_list == null || file_list.length <= 0) {
                setBusy(false);
                t.onComplete();
                return;
            }
            CachedAudios.clear();
            for (File u : file_list) {
                if (u.isFile())
                    CachedAudios.add(u.getName().toLowerCase(Locale.getDefault()));
            }
            setBusy(false);
        });
    }

    @Override
    public boolean isExistAllAudio(@NonNull String file) {
        synchronized (isBusyLock) {
            if (isBusy) {
                return false;
            }
            String res = file.toLowerCase(Locale.getDefault());
            if (!Utils.isEmpty(CachedAudios)) {
                for (String i : CachedAudios) {
                    if (i.equals(res)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public void addTag(@NonNull String path) {
        if (!setBusy(true)) {
            return;
        }
        CachedTags.add(path);
        setBusy(false);
    }

    @Override
    public void deleteTag(@NonNull String path) {
        if (!setBusy(true)) {
            return;
        }
        CachedTags.remove(path);
        setBusy(false);
    }

    @Override
    public Completable findAllTags() {
        return Completable.create(t -> {
            if (!setBusy(true)) {
                return;
            }
            List<TagDir> list = Includes.getStores().searchQueriesStore().getAllTagDirs().blockingGet();
            CachedTags.clear();
            for (TagDir u : list) {
                CachedTags.add(u.getPath());
            }
            setBusy(false);
        });
    }

    @Override
    public boolean isExistTag(@NonNull String path) {
        synchronized (isBusyLock) {
            if (isBusy) {
                return false;
            }
            if (!Utils.isEmpty(CachedTags)) {
                for (String i : CachedTags) {
                    if (i.equals(path)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
