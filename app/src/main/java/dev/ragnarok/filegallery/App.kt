package dev.ragnarok.filegallery

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.internal.ConstructorConstructor
import dev.ragnarok.filegallery.media.music.MusicPlaybackController
import dev.ragnarok.filegallery.module.GalleryNative
import dev.ragnarok.filegallery.module.rlottie.RLottieDrawable
import dev.ragnarok.filegallery.picasso.PicassoInstance
import dev.ragnarok.filegallery.settings.Settings
import dev.ragnarok.filegallery.util.Utils
import dev.ragnarok.filegallery.util.existfile.FileExistJVM
import dev.ragnarok.filegallery.util.existfile.FileExistNative

class App : Application() {
    @SuppressLint("WrongConstant")
    override fun onCreate() {
        sInstanse = this

        sApplicationHandler = Handler(mainLooper)
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(Settings.get().main().getNightMode())
        GalleryNative.loadNativeLibrary { it.printStackTrace() }
        GalleryNative.updateAppContext(this)
        GalleryNative.updateDensity { Utils.density }
        ConstructorConstructor.setLogUnsafe(Settings.get().main().isDeveloper_mode())

        if (GalleryNative.isNativeLoaded()) {
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

        @Volatile
        private var sApplicationHandler: Handler? = null

        val applicationHandler: Handler?
            get() {
                return sApplicationHandler
            }

        val instance: App
            get() {
                checkNotNull(sInstanse) { "App instance is null!!! WTF???" }
                return sInstanse!!
            }
    }
}
