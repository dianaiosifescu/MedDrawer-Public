package com.diana.test.meddrawer.view.medicine


import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.DialogInterface

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams

import androidx.recyclerview.widget.LinearLayoutManager
import com.diana.test.meddrawer.BaseActivity
import com.diana.test.meddrawer.MainActivity
import com.diana.test.meddrawer.R
import com.diana.test.meddrawer.bases.BaseView
import com.diana.test.meddrawer.model.AddMedModel
import com.diana.test.meddrawer.model.MedicineModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_medicine_v2.*
import kotlinx.android.synthetic.main.activity_medicine_v2.view.*
import kotlinx.android.synthetic.main.recyclerview_addmed_row_details.*

enum class EditMedicineState (){
    NEW,
    VIEW,
    EDIT
}

class MedicineActivity : BaseActivity(), MedicineContract.View, BaseView {

    private val adapter = MedicineAdapter(this) {
        hideKeyboard(it)
    }
    private val presenter = MedicinePresenter<MedicineContract.View>()
    private val data = Intent()
    private var state = EditMedicineState.NEW


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_v2)

        toolbar2.setNavigationIcon(R.drawable.ic_back_arrow_v2)
        toolbar2.setNavigationOnClickListener {
                onBackPressed()
        }

        setUp()
        presenter.getItemMedicine()

        tvSaveToolbar2.setOnClickListener {

            addLayout.requestFocus()
            val myToast = Toast.makeText(applicationContext,"Saved",Toast.LENGTH_SHORT)
            myToast.setGravity(Gravity.CENTER,0,0)

            when (state) {

                EditMedicineState.NEW -> {
                    this.window
                        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

                    window?.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    lottieLoading.visibility = View.VISIBLE

                    val medicine2 = adapter.saveEntry()

                    if (medicine2?.name.isNullOrEmpty()) {
                        basicAlert(it)
                        Log.e("Numele med este gol", "DA")
                    } else {
                        presenter.saveItemMedicine(medicine2)
                        state = EditMedicineState.VIEW
                        myToast.show()
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        lottieLoading.visibility = View.INVISIBLE
                        Handler().postDelayed({onBackPressed()}, 1000)
                       // onBackPressed()
//                        val intent = Intent(this, MainActivity()::class.java)
//                        startActivity(intent)

                    }
                }

                EditMedicineState.VIEW -> {
                    state = EditMedicineState.EDIT
                    updateUI()
                }

                EditMedicineState.EDIT -> {
                    this.window
                        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

                    window?.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    lottieLoading.visibility = View.VISIBLE

                    val medicine2 = adapter.saveEntry()
                    if (medicine2?.name.isNullOrEmpty()) {
                        basicAlert(it)
                    } else {
                        presenter.saveItemMedicine(medicine2)
                        myToast.show()
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        state = EditMedicineState.VIEW
                        updateUI()
                    }
                }
            }
        }

        tvDelete.setOnClickListener {
            deleteAlert(it)
        }
    }

    private fun setUp() {

        presenter.onAttach(this)
        adapter.medicine = intent.getParcelableExtra("medicine") as? MedicineModel
        val stateValue = intent.getStringExtra("state")
        state = EditMedicineState.valueOf(stateValue.toUpperCase())

        recyclerAddMed.layoutManager = LinearLayoutManager(this)
        recyclerAddMed.adapter = adapter

        updateUI()

    } //setUp

    private fun updateUI() {
        when (state) {

            EditMedicineState.NEW -> {
                adapter.makeFieldsEditable()
                adapter.notifyDataSetChanged()
                tvSaveToolbar2.text = "Save"
                tvItemState.text = "Add item"
                tvDelete.visibility = View.INVISIBLE
            }

            EditMedicineState.VIEW -> {
                adapter.makeFieldsNonEditable()
                adapter.notifyDataSetChanged()
                tvSaveToolbar2.text = "Edit"
                tvItemState.text = "Edit item"
                tvDelete.visibility = View.INVISIBLE
                lottieLoading.visibility = View.INVISIBLE

            }

            EditMedicineState.EDIT -> {
                adapter.makeFieldsEditable()
                adapter.notifyDataSetChanged()
                tvSaveToolbar2.text = "Save"
                tvDelete.visibility = View.VISIBLE
                tvItemState.text = "Edit item"
                lottieLoading.visibility = View.INVISIBLE

            }

        }
    }


    override fun gotItemMedicine(list: ArrayList<AddMedModel>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    override fun saveDone(medicine2: MedicineModel?) {
        data.putExtra("new", medicine2)
        setResult(1, data)

        adapter.medicine = medicine2
        updateUI()
    }

    override fun saveFail() {
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle(" Error saving the entry ")
            setMessage("The current medicine was not saved. Please try again!")
            setPositiveButton("OK", null)
            show()
        }
    }

    override fun deleteDone() {
        data.putExtra("delete_item", true)
        setResult(2, data)
        onBackPressed()
    }

    override fun deleteFail() {
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle(" Error deleting the entry ")
            setMessage("The current medicine was not deleted. Please try again!")
            setPositiveButton("OK", null)
            show()
        }
    }

    private fun basicAlert(view: View) {
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle(" Empty medicine name! ")
            setMessage("Enter a name for this medicine")
            setPositiveButton("OK", null)
            show()
        }
    }

    private fun deleteAlert(view: View) {
        val builder = AlertDialog.Builder(this)
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
          onBackPressed()
        }
        val positiveButtonClick = {dialog : DialogInterface, which: Int ->
            presenter.deleteItemMedicine(adapter.medicine)
        }


        with(builder)
        {
            setTitle(" Delete item ")
            setMessage("Are you sure you want to delete this medicine from your list?")
            setPositiveButton("OK", positiveButtonClick)
            setNegativeButton(android.R.string.cancel, negativeButtonClick)
            show()
        }
    }

}


