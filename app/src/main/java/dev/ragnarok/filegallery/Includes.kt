package dev.ragnarok.filegallery

import android.content.Context
import dev.ragnarok.filegallery.App.Companion.instance
import dev.ragnarok.filegallery.api.impl.Networker
import dev.ragnarok.filegallery.api.interfaces.INetworker
import dev.ragnarok.filegallery.db.impl.AppStorages
import dev.ragnarok.filegallery.db.interfaces.IStorages
import dev.ragnarok.filegallery.settings.ISettings
import dev.ragnarok.filegallery.settings.SettingsImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler

object Includes {

    @JvmStatic
    val networkInterfaces: INetworker by lazy {
        Networker(settings.main())
    }

    @JvmStatic
    val stores: IStorages by lazy {
        AppStorages(instance)
    }

    @JvmStatic
    val settings: ISettings by lazy {
        SettingsImpl.getInstance(instance)
    }

    @JvmStatic
    fun provideMainThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @JvmStatic
    fun provideApplicationContext(): Context {
        return instance
    }
}
