package com.diana.test.meddrawer.view.add

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diana.test.meddrawer.R
import com.diana.test.meddrawer.model.MedicineModel
import kotlinx.android.synthetic.main.recycler_add_search.view.*

class AddListAdapter (
    val context: Context,
    val selectListener: (model: MedicineModel) -> Unit
): RecyclerView.Adapter<AddListAdapter.AddViewHolder> () {

    var items: ArrayList<MedicineModel> = arrayListOf()


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AddViewHolder, position: Int) {
        val med = items[position]
        holder.bind(med, selectListener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddViewHolder {
        return AddViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recycler_add_search,
                parent,
                false
            )
        )
    }

    class AddViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMedName = view.tvMedName

        fun bind(med: MedicineModel, selectListener: (model: MedicineModel) -> Unit) {
            tvMedName.text = (med.name!! + " " + med.getFormText()).toUpperCase()
            itemView.setOnClickListener {
                selectListener(med)
            }
        }
    }
}

