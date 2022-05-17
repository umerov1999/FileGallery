package dev.ragnarok.filegallery.api.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import dev.ragnarok.filegallery.model.Video
import java.lang.reflect.Type

class VideoDtoAdapter : AbsAdapter(), JsonDeserializer<Video> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Video {
        if (!checkObject(json)) {
            throw JsonParseException("$TAG error parse object")
        }
        val root = json.asJsonObject
        val dto = Video()
        dto.setId(optInt(root, "id"))
        dto.setOwnerId(optInt(root, "owner_id"))
        dto.setTitle(optString(root, "title"))
        dto.setDescription(optString(root, "description"))
        dto.setDuration(optInt(root, "duration"))
        dto.setDate(optLong(root, "date"))
        dto.setRepeat(optBoolean(root, "repeat"))
        if (hasObject(root, "files")) {
            val filesRoot = root.getAsJsonObject("files")
            dto.setLink(optString(filesRoot, "mp4_720"))
        }
        if (hasArray(root, "image")) {
            val images = root.getAsJsonArray("image")
            dto.setImage(images[images.size() - 1].asJsonObject["url"].asString)
        }
        return dto
    }

    companion object {
        private val TAG = VideoDtoAdapter::class.java.simpleName
    }
}
