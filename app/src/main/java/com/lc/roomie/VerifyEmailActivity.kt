package com.lc.roomie

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.textfield.TextInputLayout
import com.google.api.DistributionOrBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_verify_email.*
import kotlinx.android.synthetic.main.activity_verify_otp.*

class VerifyEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        val email = findViewById<TextInputLayout>(R.id.inputEmail)
        val db = Firebase.firestore
        val verifyEmail = findViewById<Button>(R.id.VerifyEmail)

//        verifyEmail.isEnabled = false

//        email.addTextChangedListener(object : TextWatcher {
//            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//            override fun afterTextChanged(s: Editable?) {
//                val pattern = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])".toRegex()
//                if (pattern.matches(email.text.toString())) {
//                    verifyEmail.isEnabled = true
//                    verifyEmail.background = getDrawable(R.drawable.enabled_button_background)
//                } else {
//                    verifyEmail.isEnabled = false
//                    verifyEmail.background = getDrawable(R.drawable.disabled_button_background)
//                    email.error = "Enter a valid email address"
//                    email.requestFocus()
//                }
//
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })

        VerifyEmail.setOnClickListener{
//            db.collection("users").document("${intent.getStringExtra("docRef")}").update("email", email.text.toString())
            val intent = Intent(this, HouserulesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    companion object {
        private const val TAG = "VerifyEmailActivity"
    }

    fun onRadioButtonClicked(view: View) {}
}