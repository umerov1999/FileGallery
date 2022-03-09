package dev.ragnarok.filegallery.api.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import dev.ragnarok.filegallery.model.Video;

public class VideoDtoAdapter extends AbsAdapter implements JsonDeserializer<Video> {
    private static final String TAG = VideoDtoAdapter.class.getSimpleName();

    @Override
    public Video deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!checkObject(json)) {
            throw new JsonParseException(TAG + " error parse object");
        }
        JsonObject root = json.getAsJsonObject();
        Video dto = new Video();

        dto.setId(optInt(root, "id"));
        dto.setOwnerId(optInt(root, "owner_id"));
        dto.setTitle(optString(root, "title"));
        dto.setDescription(optString(root, "description"));
        dto.setDuration(optInt(root, "duration"));
        dto.setDate(optLong(root, "date"));
        dto.setRepeat(optBoolean(root, "repeat"));

        if (hasObject(root, "files")) {
            JsonObject filesRoot = root.getAsJsonObject("files");
            dto.setLink(optString(filesRoot, "mp4_720"));
        }

        if (hasArray(root, "image")) {
            JsonArray images = root.getAsJsonArray("image");
            dto.setImage(images.get(images.size() - 1).getAsJsonObject().get("url").getAsString());
        }
        return dto;
    }
}
