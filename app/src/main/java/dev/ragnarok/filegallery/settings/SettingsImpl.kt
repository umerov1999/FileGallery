package dev.ragnarok.filegallery.settings

import android.content.Context
import dev.ragnarok.filegallery.settings.ISettings.IMainSettings

class SettingsImpl private constructor(app: Context) : ISettings {
    private val mainSettings: IMainSettings
    override fun main(): IMainSettings {
        return mainSettings
    }

    companion object {
        @Volatile
        private var instance: SettingsImpl? = null
        fun getInstance(context: Context): SettingsImpl {
            if (instance == null) {
                synchronized(SettingsImpl::class.java) {
                    if (instance == null) {
                        instance = SettingsImpl(context.applicationContext)
                    }
                }
            }
            return instance!!
        }
    }

    init {
        mainSettings = MainSettings(app)
    }
}