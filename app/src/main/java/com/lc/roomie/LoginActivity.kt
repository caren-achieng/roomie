package com.lc.roomie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        CreateAccountBtn.setOnClickListener{
            val intent = Intent(this, EnterPhoneNumberActivity::class.java)
            startActivity(intent)
        }
    }

}