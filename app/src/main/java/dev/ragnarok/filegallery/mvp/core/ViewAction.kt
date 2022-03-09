package dev.ragnarok.filegallery.mvp.core

interface ViewAction<V> {
    fun call(view: V)
}