package dev.ragnarok.filegallery

import android.os.Build
import dev.ragnarok.filegallery.BuildConfig.*
import dev.ragnarok.filegallery.util.Utils
import java.util.*

object Constants {
    const val PICASSO_TAG = "picasso_tag"

    @JvmField
    val IS_DEBUG: Boolean = DEBUG

    @JvmField
    val USER_AGENT = String.format(
        Locale.US,
        "FileGalleryAndroid/%s-%s (Android %s; SDK %d; %s; %s; %s)",
        VERSION_NAME,
        VERSION_CODE,
        Build.VERSION.RELEASE,
        Build.VERSION.SDK_INT,
        Build.SUPPORTED_ABIS[0],
        Utils.getDeviceName(),
        "ru"
    )
    const val AUDIO_PLAYER_SERVICE_IDLE = 300000
    const val FILE_PROVIDER_AUTHORITY = APPLICATION_ID + ".file_provider"
}