package dev.ragnarok.filegallery.settings.backup

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import de.maxr1998.modernpreferences.PreferenceScreen
import dev.ragnarok.filegallery.Includes
import dev.ragnarok.filegallery.model.tags.TagFull
import dev.ragnarok.filegallery.nonNullNoEmpty

class SettingsBackup {
    private val settings: Array<SettingCollector> = arrayOf(
        //Main
        SettingCollector("app_theme", SettingTypes.TYPE_STRING),
        SettingCollector("night_switch", SettingTypes.TYPE_STRING),
        SettingCollector("theme_overlay", SettingTypes.TYPE_STRING),
        SettingCollector("delete_disabled", SettingTypes.TYPE_BOOL),
        SettingCollector("font_size", SettingTypes.TYPE_STRING),
        SettingCollector("local_media_server", SettingTypes.TYPE_STRING),
        SettingCollector("use_internal_downloader", SettingTypes.TYPE_BOOL),
        SettingCollector("video_controller_to_decor", SettingTypes.TYPE_BOOL),
        SettingCollector("video_swipes", SettingTypes.TYPE_BOOL),
        SettingCollector("download_photo_tap", SettingTypes.TYPE_BOOL),
        SettingCollector("show_photos_line", SettingTypes.TYPE_BOOL),
        SettingCollector("audio_round_icon", SettingTypes.TYPE_BOOL),
        SettingCollector("use_long_click_download", SettingTypes.TYPE_BOOL),
        SettingCollector("revert_play_audio", SettingTypes.TYPE_BOOL),
        SettingCollector("player_has_background", SettingTypes.TYPE_BOOL),
        SettingCollector("player_background", SettingTypes.TYPE_STRING),
        SettingCollector("slidr_settings", SettingTypes.TYPE_STRING),
        SettingCollector("use_stop_audio", SettingTypes.TYPE_BOOL),
        SettingCollector("audio_save_mode_button", SettingTypes.TYPE_BOOL),
        SettingCollector("show_mini_player", SettingTypes.TYPE_BOOL),
        SettingCollector("lifecycle_music_service", SettingTypes.TYPE_STRING),
        SettingCollector("ffmpeg_audio_codecs", SettingTypes.TYPE_STRING),
        SettingCollector("music_dir", SettingTypes.TYPE_STRING),
        SettingCollector("photo_dir", SettingTypes.TYPE_STRING),
        SettingCollector("video_dir", SettingTypes.TYPE_STRING),
        SettingCollector("photo_to_user_dir", SettingTypes.TYPE_BOOL),
        SettingCollector("developer_mode", SettingTypes.TYPE_BOOL),
        SettingCollector("videos_ext", SettingTypes.TYPE_STRING_SET),
        SettingCollector("photo_ext", SettingTypes.TYPE_STRING_SET),
        SettingCollector("audio_ext", SettingTypes.TYPE_STRING_SET),
        SettingCollector("max_bitmap_resolution", SettingTypes.TYPE_STRING),
        SettingCollector("max_thumb_resolution", SettingTypes.TYPE_STRING),
        SettingCollector("rendering_mode", SettingTypes.TYPE_STRING),
        SettingCollector("enable_cache_ui_anim", SettingTypes.TYPE_BOOL),
        SettingCollector("enable_dirs_files_count", SettingTypes.TYPE_BOOL),
        SettingCollector("viewpager_page_transform", SettingTypes.TYPE_STRING),
        SettingCollector("player_cover_transform", SettingTypes.TYPE_STRING),
    )

    fun doBackup(): JsonObject? {
        var has = false
        val pref =
            PreferenceScreen.getPreferences(Includes.provideApplicationContext())
        val ret = JsonObject()
        for (i in settings) {
            val temp = i.requestSetting(pref)
            if (temp != null) {
                if (!has) has = true
                ret.add(i.name, temp)
            }
        }
        val yu = Includes.stores.searchQueriesStore().getTagFull().blockingGet()
        if (yu.nonNullNoEmpty()) {
            has = true
            ret.add("tags", Gson().toJsonTree(yu))
        }
        return if (!has) null else ret
    }

    fun doRestore(ret: JsonObject?) {
        ret ?: return
        val pref =
            PreferenceScreen.getPreferences(Includes.provideApplicationContext())

        for (i in settings) {
            i.restore(pref, ret)
        }
        val yu = ret.get("tags")?.asJsonArray
        yu ?: return
        val jp: ArrayList<TagFull> = ArrayList()
        for (i in yu) {
            jp.add(Gson().fromJson(i, TagFull::class.java))
        }
        if (jp.nonNullNoEmpty()) {
            Includes.stores.searchQueriesStore().putTagFull(jp).blockingAwait()
        }
    }

    private class SettingCollector(
        val name: String,
        @SettingTypes val type: Int
    ) {
        fun restore(pref: SharedPreferences, ret: JsonObject?) {
            try {
                ret ?: return
                pref.edit().remove(name).apply()
                if (!ret.has(name)) return
                val o = ret.getAsJsonObject(name)
                if (o["type"].asInt != type) return
                when (type) {
                    SettingTypes.TYPE_BOOL -> pref.edit().putBoolean(name, o["value"].asBoolean)
                        .apply()
                    SettingTypes.TYPE_INT -> pref.edit().putInt(name, o["value"].asInt).apply()
                    SettingTypes.TYPE_STRING -> pref.edit().putString(name, o["value"].asString)
                        .apply()
                    SettingTypes.TYPE_STRING_SET -> {
                        val arr = o["array"].asJsonArray
                        if (!arr.isEmpty) {
                            val rt = HashSet<String>(arr.size())
                            for (i in arr) {
                                rt.add(i.asString)
                            }
                            pref.edit()
                                .putStringSet(name, rt)
                                .apply()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun requestSetting(pref: SharedPreferences): JsonObject? {
            if (!pref.contains(name)) {
                return null
            }
            val temp = JsonObject()
            temp.addProperty("type", type)
            when (type) {
                SettingTypes.TYPE_BOOL -> temp.addProperty("value", pref.getBoolean(name, false))
                SettingTypes.TYPE_INT -> temp.addProperty("value", pref.getInt(name, 0))
                SettingTypes.TYPE_STRING -> temp.addProperty("value", pref.getString(name, ""))
                SettingTypes.TYPE_STRING_SET -> {
                    val u = JsonArray()
                    val prSet = pref.getStringSet(name, HashSet(0)) ?: return null
                    if (prSet.isEmpty()) {
                        return null
                    }
                    for (i in prSet) {
                        u.add(i)
                    }
                    temp.add("array", u)
                }
            }
            return temp
        }
    }
}
