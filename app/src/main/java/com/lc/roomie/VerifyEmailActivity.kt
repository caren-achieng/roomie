package com.lc.roomie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_verify_email.*
import kotlinx.android.synthetic.main.activity_verify_otp.*

class VerifyEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)

        VerifyEmail.setOnClickListener{
            val intent = Intent(this, HouserulesActivity::class.java)
            startActivity(intent)
        }
    }
}