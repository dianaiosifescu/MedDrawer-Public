package com.diana.test.meddrawer.view.home

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.diana.test.meddrawer.MainActivity
import com.diana.test.meddrawer.bases.BasePresenter
import com.diana.test.meddrawer.extensions.toDate
import com.diana.test.meddrawer.model.MedicineModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

enum class SortType {
    NAME,
    EXP_DATE,
    AMOUNT_LEFT
}

class HomePresenter<V: HomeContract.View> : BasePresenter<V>(), HomeContract.Presenter<V> {
    val medicineItems: ArrayList<MedicineModel> = ArrayList()
    var sortType : SortType = SortType.NAME
    val db = Firebase.firestore

    override fun getMedicine() {

        medicineItems.clear()

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userListRef = db
            .collection("users").document(currentUser?.email!!)
            .collection("medicines")

        userListRef
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                   val a = MedicineModel("", "", 1, 1, "", 1, "").fromHashMap(document.data)
                    Log.d("Lista user!!!:", "${document.id} => ${document.data}")
                    Log.d("Obiect medicament", a.name)
                    medicineItems.add(a)
                }

                sortingMed()

                mvpView?.gotMedicine(medicineItems)
            }
            .addOnFailureListener { exception ->
                Log.d("Lista user!!!:", "Error getting documents: ", exception)
            }

    }

    override fun updateMedicine(new: MedicineModel?) {
       val index = medicineItems.indexOf(new)
        medicineItems[index] = new!!
        sortingMed()
        mvpView?.gotMedicine(medicineItems)
    }

    private fun sortingMed() {
        when (sortType) {
            SortType.NAME -> {
                medicineItems.sortWith(compareBy({it.name?.toLowerCase()}))
            }
            SortType.EXP_DATE -> {
                medicineItems.sortWith(compareBy({it.expDate?.toDate()}))
            }
            SortType.AMOUNT_LEFT -> {
                medicineItems.sortWith(compareBy({it.amountLeft}))
            }
        }
    }
}