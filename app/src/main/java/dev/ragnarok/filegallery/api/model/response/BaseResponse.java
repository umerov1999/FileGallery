package dev.ragnarok.filegallery.api.model.response;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> extends VkReponse {
    @SerializedName("response")
    public T response;
}