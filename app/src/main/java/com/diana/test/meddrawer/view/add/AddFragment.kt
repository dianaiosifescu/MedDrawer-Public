package com.diana.test.meddrawer.view.add

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.diana.test.meddrawer.BaseActivity
import com.diana.test.meddrawer.MainActivity
import com.diana.test.meddrawer.R
import com.diana.test.meddrawer.bases.BaseView
import com.diana.test.meddrawer.model.MedicineModel
import com.diana.test.meddrawer.view.medicine.MedicineActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add.*


class AddFragment: Fragment(), AddContract.View, BaseView {
    private val presenter = AddPresenter<AddContract.View>()
    private lateinit var fragmentCallback: FragmentCallback
    private val adapter : AddListAdapter by lazy {
        AddListAdapter(activity!!) {
            val intent = Intent(activity, MedicineActivity()::class.java)
            intent.putExtra("medicine", it)
            intent.putExtra("state", "NEW")
           // intent.putExtra("modifyGeneralMed", 1)
            startActivityForResult(intent,1)
        }
    }

    var originalMedList : ArrayList<MedicineModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.textViewToolbar?.text = getString(R.string.add)
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUp()
        presenter.getGeneralMeds()

        tvTypeSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                Log.e("Editare", s.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                    adapter.items = ArrayList(originalMedList.filter {
                        (it.name!!.toLowerCase() + " " + it.getFormText().toLowerCase()).startsWith(
                            s.toString().toLowerCase()
                        )
                    })

                if (s.isEmpty()) {
                    adapter.items = ArrayList()
                }
                    adapter.notifyDataSetChanged()
            }
        })

        imageX.setOnClickListener{
            tvTypeSearch.setText("")
            (activity as BaseActivity).hideKeyboard(view)
            containerSearch.requestFocus()
        }

        tvAddNew?.setOnClickListener {
            val intentAdd = Intent(activity, MedicineActivity::class.java)
            intentAdd.putExtra("state", "NEW")
            startActivityForResult(intentAdd,1)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentCallback = context as FragmentCallback
    }

    private fun setUp() {
        presenter.onAttach(this)
        recyclerViewSearch.layoutManager = LinearLayoutManager(activity!!)
        recyclerViewSearch.adapter = adapter
    }

    override fun gotGeneralMeds(list: ArrayList<MedicineModel>) {
        originalMedList = list
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activity
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val newMedicine = data?.getParcelableExtra("new") as? MedicineModel
            fragmentCallback.onMedicineAdded(newMedicine)
        }
    }
}