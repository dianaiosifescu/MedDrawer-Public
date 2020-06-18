package com.diana.test.meddrawer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.diana.test.meddrawer.model.MedicineModel
import com.diana.test.meddrawer.view.add.AddFragment
import com.diana.test.meddrawer.view.add.FragmentCallback
import com.diana.test.meddrawer.view.home.HomeFragment
import com.diana.test.meddrawer.view.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : BaseActivity(), FragmentCallback {


    var PRIVATE_MODE = 0
    val sharedPrefFile = "sorting"
    var sharedPreferences : SharedPreferences? = null
    //val homeFragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences(sharedPrefFile, PRIVATE_MODE)
        loadFragment( HomeFragment())


        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    loadFragment( HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_add -> {
                    loadFragment(AddFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    loadFragment(SettingsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }//listener
    }//onCreate

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onMedicineAdded(medicineModel: MedicineModel?) {
        HomeFragment().addNewMedicine(medicineModel!!)
    }

}
