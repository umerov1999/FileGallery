package dev.ragnarok.filegallery.settings

import android.content.Context
import dev.ragnarok.filegallery.model.LocalServerSettings
import dev.ragnarok.filegallery.model.PlayerCoverBackgroundSettings
import dev.ragnarok.filegallery.model.SlidrSettings
import dev.ragnarok.filegallery.settings.theme.ThemeOverlay
import io.reactivex.rxjava3.core.Observable

interface ISettings {
    fun main(): IMainSettings
    interface IMainSettings {
        fun getFontSize(): Int

        @ThemeOverlay
        fun getThemeOverlay(): Int
        fun getMainThemeKey(): String
        fun setMainTheme(key: String)
        fun switchNightMode(@NightMode key: Int)
        fun isDarkModeEnabled(context: Context): Boolean

        @NightMode
        fun getNightMode(): Int
        fun isDeveloper_mode(): Boolean
        fun isEnable_cache_ui_anim(): Boolean

        fun getMusicDir(): String
        fun getPhotoDir(): String
        fun getVideoDir(): String

        fun getLocalServer(): LocalServerSettings
        fun setLocalServer(settings: LocalServerSettings)
        fun getPlayerCoverBackgroundSettings(): PlayerCoverBackgroundSettings
        fun setPlayerCoverBackgroundSettings(settings: PlayerCoverBackgroundSettings)
        fun getSlidrSettings(): SlidrSettings
        fun setSlidrSettings(settings: SlidrSettings)
        fun getMusicLifecycle(): Int

        fun getMaxBitmapResolution(): Int
        fun getMaxThumbResolution(): Int
        fun getRendering_mode(): Int
        fun getFFmpegPlugin(): Int

        fun isUse_internal_downloader(): Boolean

        fun isPlayer_Has_Background(): Boolean
        fun isShow_mini_player(): Boolean

        fun observeLocalServer(): Observable<LocalServerSettings>
        fun isShow_photos_line(): Boolean
        fun isDownload_photo_tap(): Boolean
        fun isAudio_round_icon(): Boolean
        fun isPhoto_to_user_dir(): Boolean
        fun isVideo_swipes(): Boolean
        fun isVideo_controller_to_decor(): Boolean
        fun isUse_stop_audio(): Boolean
        fun isRevert_play_audio(): Boolean

        fun videoExt(): Set<String>
        fun photoExt(): Set<String>
        fun audioExt(): Set<String>

        fun getViewpager_page_transform(): Int
        fun getPlayer_cover_transform(): Int
    }
}