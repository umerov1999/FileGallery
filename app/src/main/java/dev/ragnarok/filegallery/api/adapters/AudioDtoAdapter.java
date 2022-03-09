package dev.ragnarok.filegallery.api.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import dev.ragnarok.filegallery.model.Audio;

public class AudioDtoAdapter extends AbsAdapter implements JsonDeserializer<Audio> {
    private static final String TAG = AudioDtoAdapter.class.getSimpleName();

    @Override
    public Audio deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!checkObject(json)) {
            throw new JsonParseException(TAG + " error parse object");
        }
        Audio dto = new Audio();
        JsonObject root = json.getAsJsonObject();
        dto.setId(optInt(root, "id"));
        dto.setOwnerId(optInt(root, "owner_id"));
        dto.setArtist(optString(root, "artist"));
        dto.setTitle(optString(root, "title"));
        dto.setDuration(optInt(root, "duration"));
        dto.setUrl(optString(root, "url"));

        if (hasObject(root, "album")) {
            JsonObject thmb = root.getAsJsonObject("album");

            if (hasObject(thmb, "thumb")) {
                thmb = thmb.getAsJsonObject("thumb");
                if (thmb.has("photo_600")) {
                    dto.setThumb_image(optString(thmb, "photo_600"));
                }
            }
        }
        dto.updateDownloadIndicator();

        return dto;
    }
}
