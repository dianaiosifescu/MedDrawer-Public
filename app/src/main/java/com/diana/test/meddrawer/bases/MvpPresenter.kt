package com.diana.test.meddrawer.bases

interface MvpPresenter<V : BaseView> {
    fun onAttach(view: V)
    fun onDetach()
}