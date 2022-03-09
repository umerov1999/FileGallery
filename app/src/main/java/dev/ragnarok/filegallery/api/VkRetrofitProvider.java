package dev.ragnarok.filegallery.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.ragnarok.filegallery.api.adapters.AudioDtoAdapter;
import dev.ragnarok.filegallery.api.adapters.BooleanAdapter;
import dev.ragnarok.filegallery.api.adapters.PhotoDtoAdapter;
import dev.ragnarok.filegallery.api.adapters.VideoDtoAdapter;
import dev.ragnarok.filegallery.model.Audio;
import dev.ragnarok.filegallery.model.Photo;
import dev.ragnarok.filegallery.model.Video;

public class VkRetrofitProvider {
    private static final Gson VKGSON = new GsonBuilder()
            .registerTypeAdapter(Photo.class, new PhotoDtoAdapter())
            .registerTypeAdapter(boolean.class, new BooleanAdapter())
            .registerTypeAdapter(Audio.class, new AudioDtoAdapter())
            .registerTypeAdapter(Video.class, new VideoDtoAdapter())
            .create();

    public static Gson getVkgson() {
        return VKGSON;
    }
}