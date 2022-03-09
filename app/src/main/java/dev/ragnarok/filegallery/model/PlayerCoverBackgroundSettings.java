package dev.ragnarok.filegallery.model;

import com.google.gson.annotations.SerializedName;

public class PlayerCoverBackgroundSettings {
    @SerializedName("enabled_rotation")
    public boolean enabled_rotation;

    @SerializedName("invert_rotation")
    public boolean invert_rotation;

    @SerializedName("fade_saturation")
    public boolean fade_saturation;

    @SerializedName("rotation_speed")
    public float rotation_speed;

    @SerializedName("zoom")
    public float zoom;

    @SerializedName("blur")
    public int blur;

    public PlayerCoverBackgroundSettings set_default() {
        enabled_rotation = true;
        invert_rotation = false;
        fade_saturation = true;
        rotation_speed = 0.3f;
        zoom = 1.2f;
        blur = 16;
        return this;
    }
}
