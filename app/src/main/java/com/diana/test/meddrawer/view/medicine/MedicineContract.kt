package com.diana.test.meddrawer.view.medicine;

import com.diana.test.meddrawer.bases.BaseView
import com.diana.test.meddrawer.bases.MvpPresenter
import com.diana.test.meddrawer.model.AddMedModel
import com.diana.test.meddrawer.model.MedicineModel

object MedicineContract {
    interface Presenter<V : BaseView> : MvpPresenter<V> {
        fun getItemMedicine()
        fun saveItemMedicine(med : MedicineModel?)
        fun deleteItemMedicine(med : MedicineModel?)
        }

    interface View: BaseView {
        fun gotItemMedicine(list: ArrayList<AddMedModel>)
        fun saveDone(med: MedicineModel?)
        fun saveFail()
        fun deleteDone()
        fun deleteFail()
        }
}
