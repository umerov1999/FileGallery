package dev.ragnarok.filegallery.model;

import com.google.gson.annotations.SerializedName;

public class LocalServerSettings {
    @SerializedName("url")
    public String url;

    @SerializedName("password")
    public String password;

    @SerializedName("enabled")
    public boolean enabled;

    @SerializedName("enabled_audio_local_sync")
    public boolean enabled_audio_local_sync;
}
