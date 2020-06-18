package com.diana.test.meddrawer.view.add

import com.diana.test.meddrawer.model.MedicineModel

interface FragmentCallback {
    fun onMedicineAdded(medicineModel: MedicineModel?)
}