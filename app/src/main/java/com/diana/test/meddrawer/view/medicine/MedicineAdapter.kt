package com.diana.test.meddrawer.view.medicine

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.diana.test.meddrawer.R
import com.diana.test.meddrawer.extensions.toDate
import com.diana.test.meddrawer.model.AddMedModel
import com.diana.test.meddrawer.model.MedicineModel
import kotlinx.android.synthetic.main.activity_medicine_v2.view.*
import kotlinx.android.synthetic.main.recyclerview_addmed_row_details.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class MedicineAdapter (
    val context : Context,
    val hideKeyboardListener: (view: View?) -> Unit) :
    RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> () {

    var items : ArrayList<AddMedModel> = arrayListOf()
    var medicine: MedicineModel? = null
    var medicine2 : MedicineModel? = null
    var areFieldsEditable: Boolean = false

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        return MedicineViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recyclerview_addmed_row_details,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val med= items[position]
        holder.bind(med, context, medicine, medicine2, areFieldsEditable, hideKeyboardListener)
    }

    fun makeFieldsEditable () {
        areFieldsEditable = true
        medicine2 = if (medicine == null) {
             MedicineModel(name = "", totalAmount = 0, amountLeft = 0, expDate = "", medForm = 0, comments = "" )
        } else {
            medicine?.deepCopy()
        }
    }

    fun makeFieldsNonEditable () {
        areFieldsEditable = false
    }

    fun saveEntry () : MedicineModel? {
        return medicine2
    }



    class MedicineViewHolder (view: View) : RecyclerView.ViewHolder (view) {
        val imageViewIcon = view.imageViewIcon
        val tvTitleRow = view.tvTitleRow
        val tvItemDetail = view.tvItemDetail
        val spinner = view.spinner
        val recyclerView = view.recyclerAddMed
        var cal = Calendar.getInstance()



        fun bind (med : AddMedModel, context : Context, medicine : MedicineModel?, medicine2 : MedicineModel?,  areFieldsEditable : Boolean, hideKeyboardListener: (view: View?) -> Unit) {


            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView(medicine2)
                }

            when (med.fieldType) {
                    AddMedModel.FieldType.TEXT -> {
                        tvItemDetail.setOnClickListener { }
                        tvItemDetail?.visibility = View.VISIBLE
                        spinner.visibility = View.GONE
                        tvItemDetail?.setInputType(InputType.TYPE_CLASS_TEXT)
                        tvItemDetail?.setTextColor(Color.BLACK)
                        tvItemDetail?.setHint("Type name here")
                        tvItemDetail.setText(medicine?.name)
                        makeEditable(areFieldsEditable)
                            if (areFieldsEditable) {
                                tvItemDetail.setOnFocusChangeListener { view, hasFocus ->
                                    if(!hasFocus) {
                                        medicine2?.name = tvItemDetail.text.toString()
                                    }
                                }
                            }
                    }

                    AddMedModel.FieldType.AMOUNT -> {
                        tvItemDetail.setOnClickListener { }
                        tvItemDetail?.visibility = View.VISIBLE
                        spinner.visibility = View.GONE
                        tvItemDetail?.setInputType(InputType.TYPE_CLASS_NUMBER)
                        tvItemDetail?.setTextColor(Color.BLACK)
                        tvItemDetail?.setHint("Quantity")
                        if (medicine?.name !== null)
                            tvItemDetail.setText(medicine?.totalAmount.toString())
                        makeEditable(areFieldsEditable)
                            if (areFieldsEditable) {
                                tvItemDetail.setOnFocusChangeListener { view, hasFocus ->
                                    if (!hasFocus) {
                                        if (tvItemDetail.text.toString().isNullOrEmpty()) {
                                            medicine2?.totalAmount = 0
                                        } else {
                                            medicine2?.totalAmount =
                                                tvItemDetail.text.toString().toInt()
                                        }
                                    }
                                }
                            }
                    }

                    AddMedModel.FieldType.AMOUNT_LEFT -> {
                        ArrayAdapter.createFromResource(
                            context,
                            R.array.med_amount_left_array,
                            android.R.layout.simple_spinner_item
                        ).also {
                            it.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                            spinner.adapter = it
                        }

                        tvItemDetail?.setOnClickListener {
                        }
                        spinner.visibility = View.GONE
                        tvItemDetail?.visibility = View.VISIBLE
                        tvItemDetail?.setTextColor(Color.BLACK)

                        tvItemDetail.setText(medicine?.getAmountText())
                        makeEditable(areFieldsEditable)

                        if (areFieldsEditable) {
                            spinner.visibility = View.VISIBLE
                            tvItemDetail?.visibility = View.GONE
                            spinner.setSelection(medicine?.amountLeft ?: 0)

                            spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                                override fun onNothingSelected(parent: AdapterView<*>?) {

                                }

                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    medicine2?.amountLeft = position
                                    hideKeyboardListener(view)
                                }

                            }

                            spinner.setOnTouchListener(OnTouchListener { view, event ->
                                hideKeyboardListener(view)
                                false
                            })
                        }
                    }

                    AddMedModel.FieldType.PICKER -> {
                        ArrayAdapter.createFromResource(
                            context,
                            R.array.med_type_array,
                            android.R.layout.simple_spinner_item
                        ).also {
                            it.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                            spinner.adapter = it
                        }

                        tvItemDetail?.setOnClickListener {
                        }
                        spinner.visibility = View.GONE
                        tvItemDetail?.visibility = View.VISIBLE
                        tvItemDetail.setText(medicine?.getFormText())
                        tvItemDetail?.setTextColor(Color.BLACK)
                        makeEditable(areFieldsEditable)

                        if (areFieldsEditable) {
                            spinner.visibility = View.VISIBLE
                            tvItemDetail?.visibility = View.GONE
                            spinner.setSelection(medicine?.medForm ?: 0)
                            spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                }

                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    medicine2?.medForm = position
                                }

                            }
                            spinner.setOnTouchListener(OnTouchListener { view, event ->
                                hideKeyboardListener(view)
                                false
                            })
                        }
                    }

                    AddMedModel.FieldType.DATE -> {
                       tvItemDetail.setOnClickListener {
                       }
                        spinner.visibility = View.GONE
                        tvItemDetail?.visibility = View.VISIBLE

                        //exp date with red
                        if (! medicine?.expDate.isNullOrEmpty()) {
                            if (medicine?.expDate?.toDate()!!.isBefore(LocalDate.now())) {
                                tvItemDetail?.setTextColor(Color.RED)
                            }
                        }

                        tvItemDetail?.setText(medicine?.expDate)
                        tvItemDetail?.setHint("Choose expiration date")
                        makeEditable(false)
                        tvItemDetail.isClickable = areFieldsEditable

                        if (areFieldsEditable) {
                                tvItemDetail?.setOnClickListener {
                                    DatePickerDialog(
                                        context, dateSetListener,
                                        cal.get(Calendar.YEAR),
                                        cal.get(Calendar.MONTH),
                                        cal.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                    hideKeyboardListener(it)
                                }
                            }
                    }

                    AddMedModel.FieldType.COMMENTS -> {
                        tvItemDetail.setOnClickListener { }
                        tvItemDetail?.visibility = View.VISIBLE
                        spinner.visibility = View.GONE
                        tvItemDetail?.setInputType(InputType.TYPE_CLASS_TEXT)
                        tvItemDetail?.setHint("Type comments here")
                        tvItemDetail.setText(medicine?.comments)
                        tvItemDetail?.setTextColor(Color.BLACK)
                        makeEditable(areFieldsEditable)

                        if (areFieldsEditable) {
                            tvItemDetail.setOnFocusChangeListener { view, hasFocus ->
                                if(!hasFocus) {
                                    medicine2?.comments = tvItemDetail.text.toString()
                                }
                            }
                        }
                    }
            }
            imageViewIcon.setImageResource(med.icon)
            tvTitleRow?.text = med.field

        }
        private fun makeEditable(enable : Boolean) {
            tvItemDetail.isClickable = enable
            tvItemDetail.isCursorVisible = enable
            tvItemDetail.isFocusable = enable
            tvItemDetail.isFocusableInTouchMode = enable
        }

        private fun updateDateInView(medicine2: MedicineModel?) {
            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
            tvItemDetail?.text?.apply {
                clear()
                append(sdf.format(cal.time))
            }
            medicine2?.expDate = tvItemDetail?.text.toString()
        }


    }
}
