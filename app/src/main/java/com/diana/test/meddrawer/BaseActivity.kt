package com.diana.test.meddrawer

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity


open class BaseActivity : AppCompatActivity() {

    fun hideKeyboard(view: View?) {
        val toUseView = view ?: currentFocus
        toUseView?.let {
            val h = Handler()
            h.postDelayed({
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(toUseView.windowToken, 0)
            }, 100)
        }
    }
    fun hideSoftKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}