package dev.ragnarok.filegallery.api.interfaces;

import androidx.annotation.CheckResult;

import java.util.List;

import dev.ragnarok.filegallery.model.Audio;
import dev.ragnarok.filegallery.model.Photo;
import dev.ragnarok.filegallery.model.Video;
import io.reactivex.rxjava3.core.Single;

public interface ILocalServerApi {
    @CheckResult
    Single<List<Video>> getVideos(Integer offset, Integer count, boolean reverse);

    @CheckResult
    Single<List<Audio>> getAudios(Integer offset, Integer count, boolean reverse);

    @CheckResult
    Single<List<Audio>> getDiscography(Integer offset, Integer count, boolean reverse);

    @CheckResult
    Single<List<Photo>> getPhotos(Integer offset, Integer count, boolean reverse);

    @CheckResult
    Single<List<Video>> searchVideos(String query, Integer offset, Integer count, boolean reverse);

    @CheckResult
    Single<List<Audio>> searchAudios(String query, Integer offset, Integer count, boolean reverse);

    @CheckResult
    Single<List<Audio>> searchDiscography(String query, Integer offset, Integer count, boolean reverse);

    @CheckResult
    Single<List<Photo>> searchPhotos(String query, Integer offset, Integer count, boolean reverse);

    @CheckResult
    Single<Integer> update_time(String hash);

    @CheckResult
    Single<Integer> delete_media(String hash);

    @CheckResult
    Single<String> get_file_name(String hash);

    @CheckResult
    Single<Integer> update_file_name(String hash, String name);
}
