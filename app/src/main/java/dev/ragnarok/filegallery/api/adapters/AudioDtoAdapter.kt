package dev.ragnarok.filegallery.api.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import dev.ragnarok.filegallery.model.Audio
import java.lang.reflect.Type

class AudioDtoAdapter : AbsAdapter(), JsonDeserializer<Audio?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Audio {
        if (!checkObject(json)) {
            throw JsonParseException("$TAG error parse object")
        }
        val dto = Audio()
        val root = json.asJsonObject
        dto.id = optInt(root, "id")
        dto.ownerId = optInt(root, "owner_id")
        dto.artist = optString(root, "artist")
        dto.title = optString(root, "title")
        dto.duration = optInt(root, "duration")
        dto.url = optString(root, "url")
        if (hasObject(root, "album")) {
            var thmb = root.getAsJsonObject("album")
            if (hasObject(thmb, "thumb")) {
                thmb = thmb.getAsJsonObject("thumb")
                if (thmb.has("photo_600")) {
                    dto.thumb_image = optString(thmb, "photo_600")
                }
            }
        }
        dto.updateDownloadIndicator()
        return dto
    }

    companion object {
        private val TAG = AudioDtoAdapter::class.java.simpleName
    }
}