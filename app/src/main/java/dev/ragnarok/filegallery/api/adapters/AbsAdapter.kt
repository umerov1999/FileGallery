package dev.ragnarok.filegallery.api.adapters

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import dev.ragnarok.filegallery.Constants

open class AbsAdapter {
    companion object {
        fun checkObject(element: JsonElement?): Boolean {
            return element is JsonObject
        }

        fun checkPrimitive(element: JsonElement?): Boolean {
            return element is JsonPrimitive
        }

        fun checkArray(element: JsonElement?): Boolean {
            return element is JsonArray && element.size() > 0
        }

        fun hasPrimitive(obj: JsonObject, name: String?): Boolean {
            if (obj.has(name)) {
                val element = obj[name]
                return element.isJsonPrimitive
            }
            return false
        }

        fun hasObject(obj: JsonObject, name: String?): Boolean {
            if (obj.has(name)) {
                val element = obj[name]
                return element.isJsonObject
            }
            return false
        }

        fun hasArray(obj: JsonObject, name: String?): Boolean {
            if (obj.has(name)) {
                val element = obj[name]
                return element.isJsonArray && element.asJsonArray.size() > 0
            }
            return false
        }

        @JvmOverloads
        fun optString(json: JsonObject, name: String?, fallback: String? = null): String? {
            return try {
                val element = json[name]
                if (element is JsonPrimitive) element.getAsString() else fallback
            } catch (e: Exception) {
                if (Constants.IS_DEBUG) {
                    e.printStackTrace()
                }
                fallback
            }
        }

        fun optBoolean(json: JsonObject, name: String?): Boolean {
            return try {
                val element = json[name]
                if (!checkPrimitive(element)) {
                    return false
                }
                val prim = element.asJsonPrimitive
                try {
                    prim.isBoolean && prim.asBoolean || prim.asInt == 1
                } catch (e: Exception) {
                    prim.asBoolean
                }
            } catch (e: Exception) {
                if (Constants.IS_DEBUG) {
                    e.printStackTrace()
                }
                false
            }
        }

        fun opt(array: JsonArray, index: Int): JsonElement? {
            return if (index < 0 || index >= array.size()) {
                null
            } else array[index]
        }

        @JvmOverloads
        fun optInt(json: JsonObject, name: String?, fallback: Int = 0): Int {
            return try {
                val element = json[name]
                (element as? JsonPrimitive)?.asInt ?: fallback
            } catch (e: Exception) {
                if (Constants.IS_DEBUG) {
                    e.printStackTrace()
                }
                fallback
            }
        }

        @JvmOverloads
        fun optLong(json: JsonObject, name: String?, fallback: Long = 0L): Long {
            return try {
                val element = json[name]
                (element as? JsonPrimitive)?.asLong ?: fallback
            } catch (e: Exception) {
                if (Constants.IS_DEBUG) {
                    e.printStackTrace()
                }
                fallback
            }
        }
    }
}