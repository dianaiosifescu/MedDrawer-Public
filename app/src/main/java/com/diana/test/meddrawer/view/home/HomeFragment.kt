package com.diana.test.meddrawer.view.home

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.diana.test.meddrawer.MainActivity
import com.diana.test.meddrawer.R
import com.diana.test.meddrawer.bases.BaseView
import com.diana.test.meddrawer.model.MedicineModel
import com.diana.test.meddrawer.view.medicine.MedicineActivity
import com.diana.test.meddrawer.view.medicine.MedicineAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.recyclerview_addmed_row_details.*

class HomeFragment : Fragment(), HomeContract.View, BaseView {

    private val adapter: MedicineListAdapter by lazy {
        MedicineListAdapter(activity!!) { medicine ->
            val intent = Intent(activity, MedicineActivity()::class.java)
            intent.putExtra("medicine", medicine)
            intent.putExtra("state", "VIEW")
            startActivityForResult(intent,1)
        }
   }

    private val presenter = HomePresenter<HomeContract.View>()
    var originalMedList : ArrayList<MedicineModel> = ArrayList()
    var editor : SharedPreferences.Editor? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).textViewToolbar?.text = getString(R.string.app_name)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editor =  (activity as MainActivity).sharedPreferences?.edit()

        setUp()
        textSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                Log.e("Editare", s.toString())
                adapter.items = ArrayList(originalMedList.filter {
                    it.name!!.toLowerCase().contains(s.toString().toLowerCase())
                })
                adapter.notifyDataSetChanged()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
        textX.setOnClickListener{
            adapter.items = originalMedList
            adapter.notifyDataSetChanged()
            textSearch.setText("")
            (activity as MainActivity).hideKeyboard(view)
            layoutHome.requestFocus()
        }

        spinnerSort?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (position) {
                    0 -> presenter.sortType = SortType.NAME
                    1 -> presenter.sortType = SortType.EXP_DATE
                    2 -> presenter.sortType = SortType.AMOUNT_LEFT
                }
                editor?.putString("SORT_TYPE",presenter.sortType.toString())
                editor?.apply()
                presenter.getMedicine()
                adapter.notifyDataSetChanged()
            }
        }
    }

    //region mvp

    override fun gotMedicine(list: ArrayList<MedicineModel>) {
        adapter.items = list
        originalMedList = list
        adapter.notifyDataSetChanged()

        (activity as? MainActivity)?.apply {
            imageMainSplash.visibility = View.GONE
            navView.visibility = View.VISIBLE
       }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activity
        if(requestCode == 1 && resultCode == 1) {
            //some code
            val newMedicine = data?.getParcelableExtra("new") as? MedicineModel
            presenter.updateMedicine(newMedicine)
        }
        if (requestCode == 1 && resultCode == 2) {
            val deletedItem = data?.getParcelableArrayExtra("delete item")
            presenter.getMedicine()
        }
    }

    //endregion

    //region private methods

    private fun setUp() {
        presenter.onAttach(this)
        recyclerView.layoutManager = LinearLayoutManager(activity!!)
        recyclerView.adapter = adapter

        ArrayAdapter.createFromResource(
            context!!,
            R.array.sort_types,
            R.layout.sort_spinner
        ).also {
            it.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            spinnerSort.adapter = it

            when ((activity as MainActivity).sharedPreferences!!.getString("SORT_TYPE",null)) {
                "NAME" -> spinnerSort.setSelection(0)
                "EXP_DATE" -> spinnerSort.setSelection(1)
                "AMOUNT_LEFT"-> spinnerSort.setSelection(2)
            }
        }
    }

    //endregion

    override fun onDetach() {
        super.onDetach()
        presenter.onDetach()
    }

    fun addNewMedicine(medicineModel: MedicineModel) {
        presenter.medicineItems.add(medicineModel)
    }

}
