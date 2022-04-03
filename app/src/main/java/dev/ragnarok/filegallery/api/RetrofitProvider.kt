package dev.ragnarok.filegallery.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.ragnarok.filegallery.api.adapters.AudioDtoAdapter
import dev.ragnarok.filegallery.api.adapters.BooleanAdapter
import dev.ragnarok.filegallery.api.adapters.PhotoDtoAdapter
import dev.ragnarok.filegallery.api.adapters.VideoDtoAdapter
import dev.ragnarok.filegallery.model.Audio
import dev.ragnarok.filegallery.model.Photo
import dev.ragnarok.filegallery.model.Video

object RetrofitProvider {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Photo::class.java, PhotoDtoAdapter())
        .registerTypeAdapter(Boolean::class.javaPrimitiveType, BooleanAdapter())
        .registerTypeAdapter(Audio::class.java, AudioDtoAdapter())
        .registerTypeAdapter(Video::class.java, VideoDtoAdapter())
        .create()
}