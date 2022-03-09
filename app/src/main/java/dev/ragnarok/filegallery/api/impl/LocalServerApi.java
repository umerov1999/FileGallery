package dev.ragnarok.filegallery.api.impl;

import static dev.ragnarok.filegallery.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

import dev.ragnarok.filegallery.api.ILocalServerServiceProvider;
import dev.ragnarok.filegallery.api.interfaces.ILocalServerApi;
import dev.ragnarok.filegallery.api.model.Items;
import dev.ragnarok.filegallery.api.model.response.BaseResponse;
import dev.ragnarok.filegallery.model.Audio;
import dev.ragnarok.filegallery.model.Photo;
import dev.ragnarok.filegallery.model.Video;
import dev.ragnarok.filegallery.util.Utils;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.functions.Function;

class LocalServerApi implements ILocalServerApi {

    private final ILocalServerServiceProvider service;

    LocalServerApi(ILocalServerServiceProvider service) {
        this.service = service;
    }

    static <T> Function<BaseResponse<Items<T>>, List<T>> extractResponseWithErrorHandling() {
        return response -> {
            if (nonNull(response.error)) {
                throw Exceptions.propagate(new Exception(Utils.firstNonEmptyString(response.error.errorMsg, "Error")));
            }
            if (response.response == null || Utils.isEmpty(response.response.items)) {
                return new ArrayList<>();
            }
            return response.response.items;
        };
    }

    static <T> Function<BaseResponse<T>, T> extractResponseWithErrorHandlingSimple() {
        return response -> {
            if (nonNull(response.error)) {
                throw Exceptions.propagate(new Exception(Utils.firstNonEmptyString(response.error.errorMsg, "Error")));
            }

            return response.response;
        };
    }

    @Override
    public Single<List<Video>> getVideos(Integer offset, Integer count, boolean reverse) {
        return service.provideLocalServerService()
                .flatMap(service -> service.getVideos(offset, count, reverse ? 1 : 0)
                        .map(extractResponseWithErrorHandling()));
    }

    @Override
    public Single<List<Audio>> getAudios(Integer offset, Integer count, boolean reverse) {
        return service.provideLocalServerService()
                .flatMap(service -> service.getAudios(offset, count, reverse ? 1 : 0)
                        .map(extractResponseWithErrorHandling()));
    }

    @Override
    public Single<List<Photo>> getPhotos(Integer offset, Integer count, boolean reverse) {
        return service.provideLocalServerService()
                .flatMap(service -> service.getPhotos(offset, count, reverse ? 1 : 0)
                        .map(extractResponseWithErrorHandling()));
    }

    @Override
    public Single<List<Audio>> getDiscography(Integer offset, Integer count, boolean reverse) {
        return service.provideLocalServerService()
                .flatMap(service -> service.getDiscography(offset, count, reverse ? 1 : 0)
                        .map(extractResponseWithErrorHandling()));
    }

    @Override
    public Single<List<Video>> searchVideos(String query, Integer offset, Integer count, boolean reverse) {
        return service.provideLocalServerService()
                .flatMap(service -> service.searchVideos(query, offset, count, reverse ? 1 : 0)
                        .map(extractResponseWithErrorHandling()));
    }

    @Override
    public Single<List<Photo>> searchPhotos(String query, Integer offset, Integer count, boolean reverse) {
        return service.provideLocalServerService()
                .flatMap(service -> service.searchPhotos(query, offset, count, reverse ? 1 : 0)
                        .map(extractResponseWithErrorHandling()));
    }

    @Override
    public Single<List<Audio>> searchAudios(String query, Integer offset, Integer count, boolean reverse) {
        return service.provideLocalServerService()
                .flatMap(service -> service.searchAudios(query, offset, count, reverse ? 1 : 0)
                        .map(extractResponseWithErrorHandling()));
    }

    @Override
    public Single<List<Audio>> searchDiscography(String query, Integer offset, Integer count, boolean reverse) {
        return service.provideLocalServerService()
                .flatMap(service -> service.searchDiscography(query, offset, count, reverse ? 1 : 0)
                        .map(extractResponseWithErrorHandling()));
    }

    @Override
    public Single<Integer> update_time(String hash) {
        return service.provideLocalServerService()
                .flatMap(service -> service.update_time(hash)
                        .map(extractResponseWithErrorHandlingSimple()));
    }

    @Override
    public Single<Integer> delete_media(String hash) {
        return service.provideLocalServerService()
                .flatMap(service -> service.delete_media(hash)
                        .map(extractResponseWithErrorHandlingSimple()));
    }

    @Override
    public Single<String> get_file_name(String hash) {
        return service.provideLocalServerService()
                .flatMap(service -> service.get_file_name(hash)
                        .map(extractResponseWithErrorHandlingSimple()));
    }

    @Override
    public Single<Integer> update_file_name(String hash, String name) {
        return service.provideLocalServerService()
                .flatMap(service -> service.update_file_name(hash, name)
                        .map(extractResponseWithErrorHandlingSimple()));
    }
}
