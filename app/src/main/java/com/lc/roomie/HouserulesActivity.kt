package com.lc.roomie

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_houserules.*
import kotlinx.android.synthetic.main.activity_verify_email.*

class HouserulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_houserules)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        Agree.setOnClickListener{
            val intent = Intent(this, CompleteProfileActivity::class.java)
            startActivity(intent)
    }
    }
}