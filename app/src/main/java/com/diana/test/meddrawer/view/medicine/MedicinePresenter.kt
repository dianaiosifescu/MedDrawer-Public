package com.diana.test.meddrawer.view.medicine

import android.util.Log
import com.diana.test.meddrawer.R
import com.diana.test.meddrawer.bases.BasePresenter
import com.diana.test.meddrawer.model.AddMedModel
import com.diana.test.meddrawer.model.MedicineModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_medicine_v2.*

class MedicinePresenter<V : MedicineContract.View> : BasePresenter<V>(), MedicineContract.Presenter<V> {
    private val medItems : ArrayList<AddMedModel> = ArrayList()
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    override fun getItemMedicine() {
        medItems.add(AddMedModel(R.drawable.ic_check_square, "Name", null, AddMedModel.FieldType.TEXT))
        medItems.add(AddMedModel(R.drawable.ic_med_total_amount, "Total amount", null, AddMedModel.FieldType.AMOUNT))
        medItems.add(AddMedModel(R.drawable.ic_med_amount_left, "Amount left", null, AddMedModel.FieldType.AMOUNT_LEFT))
        medItems.add(AddMedModel(R.drawable.ic_med_form, "Form of medicine", null, AddMedModel.FieldType.PICKER))
        medItems.add(AddMedModel(R.drawable.ic_med_exp, "Expiration date", null, AddMedModel.FieldType.DATE))
        medItems.add(AddMedModel(R.drawable.ic_med_comments, "Comments ",null, AddMedModel.FieldType.COMMENTS))

        mvpView?.gotItemMedicine(medItems)
    }

    override fun saveItemMedicine(medicine2 : MedicineModel?) {
            val medHashMap = medicine2?.toHashMap()
            val medicineRef = db
                .collection("users").document(currentUser?.email!!)
                .collection("medicines").document(medicine2?.guid!!)

            val nameGeneral = medicine2.name!!.toUpperCase() + " " + medicine2.getFormText().toUpperCase()
            val medRefGeneral = db
                .collection("allMedicines").document(nameGeneral)

            medicineRef
                .set(medHashMap!!)
                .addOnSuccessListener {
                    Log.d("Documentul salvat", "DocumentSnapshot successfully added in DB!")
                    val medHashMapGeneral = medicine2?.toHashMapGeneral()
                    medRefGeneral.set(medHashMapGeneral)
                    mvpView?.saveDone(medicine2)
                }
                .addOnFailureListener{
                        e -> Log.w("Documentul de salvat", "Error saving document", e)
                    mvpView?.saveFail()
                }
        }

    override fun deleteItemMedicine(medicine: MedicineModel?) {
        val medicineRef = db
            .collection("users").document(currentUser?.email!!)
            .collection("medicines").document(medicine?.guid!!)

        medicineRef
            .delete()
            .addOnSuccessListener {
                Log.d("Documentul sters", "DocumentSnapshot successfully deleted!")
                mvpView?.deleteDone()
            }
            .addOnFailureListener {
                    e -> Log.w("Documentul de sters", "Error deleting document", e)
                mvpView?.deleteFail()
            }
    }

    }