package dev.ragnarok.filegallery.model

import com.google.gson.annotations.SerializedName

class LocalServerSettings {
    @SerializedName("url")
    var url: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("enabled")
    var enabled = false

    @SerializedName("enabled_audio_local_sync")
    var enabled_audio_local_sync = false
}