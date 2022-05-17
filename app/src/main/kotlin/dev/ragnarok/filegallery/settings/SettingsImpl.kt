package dev.ragnarok.filegallery.settings

import android.content.Context
import dev.ragnarok.filegallery.settings.ISettings.IMainSettings

class SettingsImpl(app: Context) : ISettings {
    private val mainSettings: IMainSettings
    override fun main(): IMainSettings {
        return mainSettings
    }

    init {
        mainSettings = MainSettings(app)
    }
}
