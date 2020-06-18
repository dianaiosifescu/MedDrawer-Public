package com.diana.test.meddrawer.view.add

import android.util.Log
import com.diana.test.meddrawer.bases.BasePresenter
import com.diana.test.meddrawer.model.MedicineModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddPresenter<V: AddContract.View> : BasePresenter<V>(), AddContract.Presenter<V> {
    private val medicineItems: ArrayList<MedicineModel> = ArrayList()

    val db = Firebase.firestore

    override fun getGeneralMeds() {

        medicineItems.clear()

        val auth = FirebaseAuth.getInstance()
        //val currentUser = auth.currentUser
        val medGeneralListRef = db
            .collection("allMedicines")

        medGeneralListRef
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val a = MedicineModel().fromHashMapGeneral(document.data)
                    Log.d("Lista user!!!:", "${document.id} => ${document.data}")
                    Log.d("Obiect medicament", a.name)
                    //a.name = document.id
                    medicineItems.add(a)
                }
                mvpView?.gotGeneralMeds(medicineItems)
            }
            .addOnFailureListener { exception ->
                Log.d("Lista user!!!:", "Error getting documents: ", exception)
            }

    }
    override fun pressedButtonAdd(new: MedicineModel) {

    }
}