package dev.ragnarok.filegallery.mvp.core

interface RetPresenterAction<P : IPresenter<V>, V : IMvpView, T> {
    fun call(presenter: P): T
}