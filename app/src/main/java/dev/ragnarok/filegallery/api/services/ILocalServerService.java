package dev.ragnarok.filegallery.api.services;

import dev.ragnarok.filegallery.api.model.Items;
import dev.ragnarok.filegallery.api.model.response.BaseResponse;
import dev.ragnarok.filegallery.model.Audio;
import dev.ragnarok.filegallery.model.Photo;
import dev.ragnarok.filegallery.model.Video;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ILocalServerService {

    @FormUrlEncoded
    @POST("audio.get")
    Single<BaseResponse<Items<Audio>>> getAudios(@Field("offset") Integer offset,
                                                 @Field("count") Integer count,
                                                 @Field("reverse") Integer reverse);

    @FormUrlEncoded
    @POST("discography.get")
    Single<BaseResponse<Items<Audio>>> getDiscography(@Field("offset") Integer offset,
                                                      @Field("count") Integer count,
                                                      @Field("reverse") Integer reverse);

    @FormUrlEncoded
    @POST("photos.get")
    Single<BaseResponse<Items<Photo>>> getPhotos(@Field("offset") Integer offset,
                                                 @Field("count") Integer count,
                                                 @Field("reverse") Integer reverse);

    @FormUrlEncoded
    @POST("video.get")
    Single<BaseResponse<Items<Video>>> getVideos(@Field("offset") Integer offset,
                                                 @Field("count") Integer count,
                                                 @Field("reverse") Integer reverse);

    @FormUrlEncoded
    @POST("audio.search")
    Single<BaseResponse<Items<Audio>>> searchAudios(@Field("q") String query,
                                                    @Field("offset") Integer offset,
                                                    @Field("count") Integer count,
                                                    @Field("reverse") Integer reverse);

    @FormUrlEncoded
    @POST("discography.search")
    Single<BaseResponse<Items<Audio>>> searchDiscography(@Field("q") String query,
                                                         @Field("offset") Integer offset,
                                                         @Field("count") Integer count,
                                                         @Field("reverse") Integer reverse);

    @FormUrlEncoded
    @POST("video.search")
    Single<BaseResponse<Items<Video>>> searchVideos(@Field("q") String query,
                                                    @Field("offset") Integer offset,
                                                    @Field("count") Integer count,
                                                    @Field("reverse") Integer reverse);

    @FormUrlEncoded
    @POST("photos.search")
    Single<BaseResponse<Items<Photo>>> searchPhotos(@Field("q") String query,
                                                    @Field("offset") Integer offset,
                                                    @Field("count") Integer count,
                                                    @Field("reverse") Integer reverse);

    @FormUrlEncoded
    @POST("update_time")
    Single<BaseResponse<Integer>> update_time(@Field("hash") String hash);

    @FormUrlEncoded
    @POST("delete_media")
    Single<BaseResponse<Integer>> delete_media(@Field("hash") String hash);

    @FormUrlEncoded
    @POST("get_file_name")
    Single<BaseResponse<String>> get_file_name(@Field("hash") String hash);

    @FormUrlEncoded
    @POST("update_file_name")
    Single<BaseResponse<Integer>> update_file_name(@Field("hash") String hash, @Field("name") String name);
}
