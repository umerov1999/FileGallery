package dev.ragnarok.filegallery.settings.backup

import androidx.annotation.Keep
import de.maxr1998.modernpreferences.PreferenceScreen
import dev.ragnarok.filegallery.Includes
import dev.ragnarok.filegallery.kJson
import dev.ragnarok.filegallery.model.tags.TagFull
import dev.ragnarok.filegallery.nonNullNoEmpty
import dev.ragnarok.filegallery.util.serializeble.json.JsonObject
import dev.ragnarok.filegallery.util.serializeble.json.JsonObjectBuilder
import dev.ragnarok.filegallery.util.serializeble.prefs.Preferences
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

class SettingsBackup {
    @Keep
    @Serializable
    @Suppress("unused")
    class AppPreferencesList {
        //Main
        var app_theme: String? = null
        var night_switch: String? = null
        var theme_overlay: String? = null
        var delete_disabled: Boolean = false
        var font_size: String? = null
        var local_media_server: String? = null
        var use_internal_downloader: Boolean = false
        var video_controller_to_decor: Boolean = false
        var video_swipes: Boolean = false
        var download_photo_tap: Boolean = false
        var show_photos_line: Boolean = false
        var audio_round_icon: Boolean = false
        var use_long_click_download: Boolean = false
        var revert_play_audio: Boolean = false
        var player_has_background: Boolean = false
        var player_background: String? = null
        var slidr_settings: String? = null
        var use_stop_audio: Boolean = false
        var audio_save_mode_button: Boolean = false
        var show_mini_player: Boolean = false
        var lifecycle_music_service: String? = null
        var ffmpeg_audio_codecs: String? = null
        var music_dir: String? = null
        var photo_dir: String? = null
        var video_dir: String? = null
        var photo_to_user_dir: Boolean = false
        var developer_mode: Boolean = false
        var videos_ext: Set<String>? = null
        var photo_ext: Set<String>? = null
        var audio_ext: Set<String>? = null
        var max_bitmap_resolution: String? = null
        var max_thumb_resolution: String? = null
        var rendering_mode: String? = null
        var enable_cache_ui_anim: Boolean = false
        var enable_dirs_files_count: Boolean = false
        var viewpager_page_transform: String? = null
        var player_cover_transform: String? = null
    }

    fun doBackup(): JsonObject {
        val pref =
            PreferenceScreen.getPreferences(Includes.provideApplicationContext())
        val preferences = Preferences(pref)
        val ret = JsonObjectBuilder()
        ret.put(
            "app",
            kJson.encodeToJsonElement(
                AppPreferencesList.serializer(),
                preferences.decode(AppPreferencesList.serializer(), "")
            )
        )
        val yu = Includes.stores.searchQueriesStore().getTagFull().blockingGet()
        if (yu.nonNullNoEmpty()) {
            ret.put("tags", kJson.encodeToJsonElement(ListSerializer(TagFull.serializer()), yu))
        }
        return ret.build()
    }

    fun doRestore(ret: JsonObject?) {
        ret ?: return
        val pref =
            PreferenceScreen.getPreferences(Includes.provideApplicationContext())

        val preferences = Preferences(pref)

        ret["app"]?.let {
            preferences.encode(
                AppPreferencesList.serializer(),
                "",
                kJson.decodeFromJsonElement(AppPreferencesList.serializer(), it)
            )
        }
        ret["tags"]?.let {
            val tagsList: List<TagFull> =
                kJson.decodeFromJsonElement(ListSerializer(TagFull.serializer()), it)
            if (tagsList.nonNullNoEmpty()) {
                for (i in tagsList) {
                    i.reverseList()
                }
                Includes.stores.searchQueriesStore().putTagFull(tagsList.reversed()).blockingAwait()
            }
        }
    }
}
