package dev.ragnarok.filegallery.api.model.response

import com.google.gson.annotations.SerializedName

class BaseResponse<T> : ErrorReponse() {
    @SerializedName("response")
    var response: T? = null
}