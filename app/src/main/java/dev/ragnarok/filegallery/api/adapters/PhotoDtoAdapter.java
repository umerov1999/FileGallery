package dev.ragnarok.filegallery.api.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import dev.ragnarok.filegallery.model.Photo;

public class PhotoDtoAdapter extends AbsAdapter implements JsonDeserializer<Photo> {
    private static final String TAG = PhotoDtoAdapter.class.getSimpleName();

    @Override
    public Photo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!checkObject(json)) {
            throw new JsonParseException(TAG + " error parse object");
        }
        Photo photo = new Photo();
        JsonObject root = json.getAsJsonObject();

        photo.setId(optInt(root, "id"));
        photo.setDate(optLong(root, "date"));
        photo.setOwnerId(optInt(root, "owner_id"));
        photo.setText(optString(root, "text"));

        if (hasArray(root, "sizes")) {
            JsonArray sizesArray = root.getAsJsonArray("sizes");

            for (int i = 0; i < sizesArray.size(); i++) {
                if (!checkObject(sizesArray.get(i))) {
                    continue;
                }
                JsonObject p = sizesArray.get(i).getAsJsonObject();
                if (optString(p, "type").equals("w")) {
                    photo.setPhoto_url(optString(p, "url"));
                } else if (optString(p, "type").equals("s")) {
                    photo.setPreview_url(optString(p, "url"));
                }
            }
        }

        return photo;
    }
}