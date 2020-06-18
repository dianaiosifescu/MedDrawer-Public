package com.diana.test.meddrawer.view.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.diana.test.meddrawer.LogInActivity
import com.diana.test.meddrawer.MainActivity
import com.diana.test.meddrawer.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment(){
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings,container,false )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        activity?.textViewToolbar?.text = getString(R.string.settings)
        super.onActivityCreated(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        tvNameAccount.text = currentUser!!.email.toString()

        tvSignOut.setOnClickListener {
            auth.signOut()
            val intentLogin = Intent(context, LogInActivity::class.java)
            startActivity(intentLogin)
        }
    }
}