package com.lc.roomie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_verify_email.*
import kotlinx.android.synthetic.main.activity_verify_otp.*

class VerifyEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)

        val email = findViewById<EditText>(R.id.inputEmail)
        val db = Firebase.firestore

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
}