package com.diana.test.meddrawer.view.home

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.diana.test.meddrawer.R
import com.diana.test.meddrawer.extensions.toDate
import com.diana.test.meddrawer.model.MedicineModel
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*
import java.time.LocalDate

class MedicineListAdapter(
    val context: Context,
    val selectListener: (model: MedicineModel) -> Unit
) : RecyclerView.Adapter<MedicineListAdapter.MedViewHolder> () {
    var items: ArrayList<MedicineModel> = arrayListOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedViewHolder {
        return MedViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recyclerview_item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MedViewHolder, position: Int) {
        val med = items[position]
        holder.bind(med, selectListener)
    }

    class MedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMedicineItem = view.tv_medicine_title
        val tvExpDate = view.textViewDate
        val ivMedType = view.imageView
        val tvMedAmount = view.tvTotalAmount
        val ivMedAmountLeft = view.imAmountLeft


        fun bind(med: MedicineModel, selectListener: (model: MedicineModel) -> Unit) {
            when (med.medForm) {
                0 -> ivMedType.setImageResource(R.drawable.ic_med_pill)
                1 -> ivMedType.setImageResource(R.drawable.ic_pill)
                2 -> ivMedType.setImageResource(R.drawable.ic_med_bandaid)
                3 -> ivMedType.setImageResource(R.drawable.ic_syrup)
                4 -> ivMedType.setImageResource(R.drawable.ic_med_cream)
                5 -> ivMedType.setImageResource(R.drawable.ic_med_gel)
                6 -> ivMedType.setImageResource(R.drawable.ic_med_liquid)
                7 -> ivMedType.setImageResource(R.drawable.ic_med_drink)
                8 -> ivMedType.setImageResource(R.drawable.ic_suppository)
                9 -> ivMedType.setImageResource(R.drawable.ic_med_spray)
            }
            tvMedicineItem?.text = med.name!!.toUpperCase()

            if (! med.expDate.isNullOrEmpty()) {
                if (med?.expDate?.toDate()!!.isBefore(LocalDate.now())) {
                    tvExpDate?.setTextColor(Color.RED)
                } else {
                    tvExpDate?.setTextColor(Color.parseColor("#737373"))
                }
            }
            tvExpDate?.text = med.expDate

            tvMedAmount?.text = med.totalAmount.toString()

            when (med.amountLeft) {
                0 -> ivMedAmountLeft.setImageResource(R.drawable.ic_bars)
                1 -> ivMedAmountLeft.setImageResource(R.drawable.ic_bars_75)
                2 -> ivMedAmountLeft.setImageResource(R.drawable.ic_bars_half)
                3 -> ivMedAmountLeft.setImageResource(R.drawable.ic_bars_almost_empty)
            }
            itemView.setOnClickListener {
                selectListener(med)
            }

        }

    }
}