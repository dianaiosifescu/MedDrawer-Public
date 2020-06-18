package com.diana.test.meddrawer.bases

open class BasePresenter<V : BaseView> : MvpPresenter<V> {
    var mvpView: V? = null

    override fun onAttach(view: V) {
        this.mvpView = view
    }

    override fun onDetach() {
        mvpView = null
    }
}