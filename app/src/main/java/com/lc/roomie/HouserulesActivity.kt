package com.lc.roomie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_houserules.*
import kotlinx.android.synthetic.main.activity_verify_email.*

class HouserulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_houserules)

        Agree.setOnClickListener{
            val intent = Intent(this, CompleteProfileActivity::class.java)
            startActivity(intent)
    }
    }
}