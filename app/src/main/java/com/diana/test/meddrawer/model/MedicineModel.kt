package com.diana.test.meddrawer.model

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.HashMap

@Parcelize
class MedicineModel (
    var guid: String = UUID.randomUUID().toString(),
    var name : String? = null,
    var totalAmount : Int = 1,
    var amountLeft : Int = 0,
    var expDate : String? = null,
    var medForm : Int = 0,
    var comments :String? = null
): Parcelable {

    private val amountStringValues = listOf("full", "75%", "50%", "almost empty")
    private val medFormStringValues = listOf("none", "pill", "bandaid", "syrup", "cream", "gel", "drops", "drink", "suppository", "spray")

    //fun convertToDate()

    fun getAmountText () : String {
        return amountStringValues[amountLeft]
    }

    fun getFormText () : String {
        return medFormStringValues[medForm]
    }

    fun deepCopy():MedicineModel {
        val JSON = Gson().toJson(this)
        return Gson().fromJson(JSON, MedicineModel::class.java)
    }

    fun toHashMap() : HashMap<String, Any?> {
        return hashMapOf(
            "id" to guid,
            "name" to name,
            "total amount" to totalAmount,
            "amount left" to amountLeft,
            "expiration date" to expDate,
            "medicine type" to medForm,
            "comments" to comments
        )
    }

    fun fromHashMap(hashMap : Map<String, Any> ) : MedicineModel {
        guid = hashMap["id"] as String
        name = hashMap["name"] as String
        totalAmount = (hashMap["total amount"] as Long).toInt()
        amountLeft = (hashMap["amount left"] as Long).toInt()
        expDate = hashMap["expiration date"] as String?
        medForm = (hashMap["medicine type"] as Long).toInt()
        comments = hashMap["comments"] as String?

        return this
    }

    fun toHashMapGeneral() : HashMap<String, Any?> {
        return hashMapOf(
            "name" to name,
            "medicine type" to medForm
        )
    }

    fun fromHashMapGeneral(hashMap : Map<String, Any> ) : MedicineModel {
        name = hashMap["name"] as String
        medForm = (hashMap["medicine type"] as Long).toInt()
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other is MedicineModel) {
            return guid == other.guid
        } else {
            return false
        }
    }
}