package dev.ragnarok.filegallery.mvp.core

interface PresenterAction<P : IPresenter<V>, V : IMvpView> {
    fun call(presenter: P)
}