package dev.ragnarok.filegallery.api.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import dev.ragnarok.filegallery.model.Photo
import java.lang.reflect.Type

class PhotoDtoAdapter : AbsAdapter(), JsonDeserializer<Photo> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Photo {
        if (!checkObject(json)) {
            throw JsonParseException("$TAG error parse object")
        }
        val photo = Photo()
        val root = json.asJsonObject
        photo.id = optInt(root, "id")
        photo.date = optLong(root, "date")
        photo.ownerId = optInt(root, "owner_id")
        photo.text = optString(root, "text")
        if (hasArray(root, "sizes")) {
            val sizesArray = root.getAsJsonArray("sizes")
            for (i in 0 until sizesArray.size()) {
                if (!checkObject(sizesArray[i])) {
                    continue
                }
                val p = sizesArray[i].asJsonObject
                if (optString(p, "type").equals("w")) {
                    photo.photo_url = optString(p, "url")
                } else if (optString(p, "type").equals("s")) {
                    photo.preview_url = optString(p, "url")
                }
            }
        }
        return photo
    }

    companion object {
        private val TAG = PhotoDtoAdapter::class.java.simpleName
    }
}
