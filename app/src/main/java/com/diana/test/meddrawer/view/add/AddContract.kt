package com.diana.test.meddrawer.view.add

import com.diana.test.meddrawer.bases.BaseView
import com.diana.test.meddrawer.bases.MvpPresenter
import com.diana.test.meddrawer.model.MedicineModel

object AddContract {
    interface Presenter<V : BaseView> : MvpPresenter<V> {

        fun pressedButtonAdd(new: MedicineModel)
        fun getGeneralMeds()
    }

    interface View: BaseView {
        fun gotGeneralMeds(list: ArrayList<MedicineModel>)
    }
}