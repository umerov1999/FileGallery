package dev.ragnarok.filegallery

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dev.ragnarok.filegallery.activity.crash.CrashUtils
import dev.ragnarok.filegallery.media.music.MusicPlaybackController
import dev.ragnarok.filegallery.module.GalleryNative
import dev.ragnarok.filegallery.module.rlottie.RLottieDrawable
import dev.ragnarok.filegallery.picasso.PicassoInstance
import dev.ragnarok.filegallery.settings.Settings
import dev.ragnarok.filegallery.util.Utils
import dev.ragnarok.filegallery.util.existfile.FileExistJVM
import dev.ragnarok.filegallery.util.existfile.FileExistNative

class App : Application() {
    override fun onCreate() {
        sInstanse = this

        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(Settings.get().main().getNightMode())
        CrashUtils.install(this)

        GalleryNative.loadNativeLibrary(object : GalleryNative.NativeOnException {
            override fun onException(e: Error) {
                e.printStackTrace()
            }
        })
        GalleryNative.updateAppContext(this)
        GalleryNative.updateDensity(object : GalleryNative.OnGetDensity {
            override fun get(): Float {
                return Utils.density
            }
        })

        if (GalleryNative.isNativeLoaded) {
            MusicPlaybackController.tracksExist = FileExistNative()
        } else {
            MusicPlaybackController.tracksExist = FileExistJVM()
        }
        MusicPlaybackController.registerBroadcast(this)

        RLottieDrawable.setCacheResourceAnimation(Settings.get().main().isEnable_cache_ui_anim())
        PicassoInstance.init(this)
    }

    companion object {
        @Volatile
        private var sInstanse: App? = null

        val instance: App
            get() {
                checkNotNull(sInstanse) { "App instance is null!!! WTF???" }
                return sInstanse!!
            }
    }
}
