package com.diana.test.meddrawer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*


class LogInActivity : BaseActivity() {
    private lateinit var auth : FirebaseAuth
    private var email : String? = null
    private var password : String? = null
    private var password2 : String? = null

    val db = Firebase.firestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        tvLogin.setOnClickListener {
            hideKeyboard(it)
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            imageLoadingLogin.visibility = View.VISIBLE
            login()
        }

        tvSignUpClick.setOnClickListener {
            signUpPage()
        }

        tvRegister.setOnClickListener {
            //tvInvalidLogin.visibility = View.INVISIBLE
            hideKeyboard(it)
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            imageLoadingLogin.visibility = View.VISIBLE
            createNewUser()
        }
        tvSignInClick.setOnClickListener {
            signInPage()
        }
        tvForgetPassword.setOnClickListener {
            forgetPassword()
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
            Log.e("USER LOGGED IN", currentUser.email)
        } else {
            splash_login.visibility = View.INVISIBLE
        }
    }

    private fun signInPage() {
        etEmail.setText("")
        etPassword.setText("")
        etEmail.requestFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0)
        etConfirmPassword.visibility = View.INVISIBLE
        tvForgetPassword.visibility = View.VISIBLE
        tvLogin.visibility = View.VISIBLE
        tvSignUp.visibility = View.VISIBLE
        tvSignUpClick.visibility = View.VISIBLE
        tvRegister.visibility = View.INVISIBLE
        tvSignIn.visibility = View.INVISIBLE
        tvSignInClick.visibility = View.INVISIBLE
        etPassword.visibility = View.VISIBLE
        tvSendResetPassword.visibility=View.INVISIBLE
        tvResetPassword.visibility=View.INVISIBLE
        tvInvalidLogin.visibility = View.INVISIBLE


    }
    private fun signUpPage() {
        etConfirmPassword.visibility = View.VISIBLE
        tvForgetPassword.visibility = View.INVISIBLE
        tvLogin.visibility = View.INVISIBLE
        tvSignUp.visibility = View.INVISIBLE
        tvSignUpClick.visibility = View.INVISIBLE
        tvRegister.visibility = View.VISIBLE
        tvSignIn.visibility = View.VISIBLE
        tvSignInClick.visibility = View.VISIBLE
        tvInvalidLogin.visibility = View.INVISIBLE

        etEmail.setText("")
        etPassword.setText("")
        etConfirmPassword.setText("")
        etEmail.requestFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0)
    }

    private fun createNewUser() {
        email = etEmail.text.toString()
        password = etPassword.text.toString()
        password2 = etConfirmPassword.text.toString()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && email!!.contains("@") && email!!.length > 6 && password == password2 && password!!.length > 5) {
            auth.createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e("NEW USER CREATION", "createUserWithEmail:success")
                        val user = auth.currentUser
                        val userData = hashMapOf(
                                    "ID" to user?.uid
                        )
                        val userRef = db.collection("users").document(user?.email!!)
                        userRef.set(userData)
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e("NEW USER CREATION", "createUserWithEmail:failure", it.exception)

                        tvInvalidLogin.visibility = View.VISIBLE
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        imageLoadingLogin.visibility = View.INVISIBLE
                        //updateUI(null)
                    }
                }
        } else {
            if (password != password2) {
                tvInvalidLogin.text = "Passwords do not match!"
                Log.e("Password", "Passwords do not match!")
                tvInvalidLogin.visibility = View.VISIBLE
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                imageLoadingLogin.visibility = View.INVISIBLE
            } else {
                if (password!!.length < 6) {
                    tvInvalidLogin.text = "Password must be at least 6 characters long!"
                    tvInvalidLogin.visibility = View.VISIBLE
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    imageLoadingLogin.visibility = View.INVISIBLE
                }
            else {
                Log.e("Error log on", " Short password")
                tvInvalidLogin.text = "Invalid email or password!"
                tvInvalidLogin.visibility = View.VISIBLE
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    imageLoadingLogin.visibility = View.INVISIBLE
            }
        }
        }
    }

    private fun login() {
        email = etEmail.text.toString()
        password = etPassword.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && email!!.contains("@") && email!!.length > 6 && password!!.length > 5) {

            auth.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG!!!!!!", "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG!!!!!", "signInWithEmail:failure", it.exception)
                        tvInvalidLogin.visibility = View.VISIBLE
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        imageLoadingLogin.visibility = View.INVISIBLE
                    }
                }
        } else {
            tvInvalidLogin.visibility = View.VISIBLE
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            imageLoadingLogin.visibility = View.INVISIBLE
        }
    }

    private fun forgetPassword() {
        etEmail.setText("")
        etEmail.requestFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0)

        etPassword.visibility=View.INVISIBLE
        etConfirmPassword.visibility = View.INVISIBLE
        tvForgetPassword.visibility = View.INVISIBLE
        tvLogin.visibility = View.INVISIBLE
        tvSignUp.visibility = View.INVISIBLE
        tvSignUpClick.visibility = View.INVISIBLE
        tvRegister.visibility = View.INVISIBLE
        tvSignIn.visibility = View.INVISIBLE
        tvSignInClick.visibility = View.VISIBLE
        tvSendResetPassword.visibility=View.VISIBLE
        tvResetPassword.visibility=View.VISIBLE
        tvInvalidLogin.visibility = View.INVISIBLE


        tvSendResetPassword.setOnClickListener {
            hideKeyboard(it)
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            imageLoadingLogin.visibility = View.VISIBLE

          var emailAddress = etEmail.text?.toString()
            if (!TextUtils.isEmpty(emailAddress)) {
                auth.sendPasswordResetEmail(emailAddress!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "Email sent for password reset")
                            signInPage()
                        }
                    }
                    .addOnFailureListener{
                        Log.e("Email not sent", it.toString())
                        tvInvalidLogin.text = "Error resetting your password. Please try again."
                        tvInvalidLogin.visibility = View.VISIBLE
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        imageLoadingLogin.visibility = View.INVISIBLE
                    }
            } else {
                Log.e("TAG", "Empty email address!!!")
                tvInvalidLogin.text = "Enter your email address!"
                tvInvalidLogin.visibility = View.VISIBLE
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                imageLoadingLogin.visibility = View.INVISIBLE
            }
        }



    }

    private fun updateUI(user : FirebaseUser?) {
        val intentLogin = Intent(this, MainActivity::class.java)
        startActivity(intentLogin)
    }

}