package dev.ragnarok.filegallery.util.rxutils

import dev.ragnarok.filegallery.Constants
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer

object RxUtils {
    private val DUMMMY_ACTION_0 = Action {}
    fun dummy(): Action {
        return DUMMMY_ACTION_0
    }

    inline fun <reified T : Any> ignore(): Consumer<T> {
        return Consumer { t: T ->
            if (t is Throwable && Constants.IS_DEBUG) {
                (t as Throwable).printStackTrace()
            }
        }
    }
}
