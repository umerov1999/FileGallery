package dev.ragnarok.filegallery.api.model;

import com.google.gson.annotations.SerializedName;


public class Error {
    @SerializedName("error_msg")
    public String errorMsg;

    @SerializedName("method")
    public String method;
}
