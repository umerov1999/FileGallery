package dev.ragnarok.filegallery.api.model

import com.google.gson.annotations.SerializedName

class Error {
    @SerializedName("error_msg")
    var errorMsg: String? = null

    @SerializedName("method")
    var method: String? = null
}