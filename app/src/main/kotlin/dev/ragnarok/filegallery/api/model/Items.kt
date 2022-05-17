package dev.ragnarok.filegallery.api.model

import com.google.gson.annotations.SerializedName

class Items<I> {
    @SerializedName("count")
    var count = 0

    @SerializedName("items")
    var items: MutableList<I>? = null
}