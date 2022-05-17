package dev.ragnarok.filegallery.api.model.response

import com.google.gson.annotations.SerializedName
import dev.ragnarok.filegallery.api.model.Error

open class ErrorReponse {
    @SerializedName("error")
    var error: Error? = null
}