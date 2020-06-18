package com.diana.test.meddrawer.view.home

import com.diana.test.meddrawer.bases.BaseView
import com.diana.test.meddrawer.bases.MvpPresenter
import com.diana.test.meddrawer.model.MedicineModel

object HomeContract {
    interface Presenter<V : BaseView>: MvpPresenter<V> {
        fun getMedicine()
        fun updateMedicine(new : MedicineModel?)
    }

    interface View: BaseView {
        fun gotMedicine(list: ArrayList<MedicineModel>)

    }
}
