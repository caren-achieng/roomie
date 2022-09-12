package com.lc.roomie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_send_otp.*
import kotlinx.android.synthetic.main.activity_verify_email.*
import kotlinx.android.synthetic.main.activity_verify_otp.*

class VerifyOTPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        buttonVefiry.setOnClickListener{
            val intent = Intent(this, VerifyEmailActivity::class.java)
            startActivity(intent)
        }
    }
}