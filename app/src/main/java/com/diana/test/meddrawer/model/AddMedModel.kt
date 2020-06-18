package com.diana.test.meddrawer.model

class AddMedModel (
    var icon : Int = 0,
    var field : String? = null,
    var fieldContent : String? = null,
    var fieldType : FieldType)
{
    enum class FieldType {
        TEXT,
        AMOUNT,
        AMOUNT_LEFT,
        DATE,
        PICKER,
        COMMENTS
    }
}