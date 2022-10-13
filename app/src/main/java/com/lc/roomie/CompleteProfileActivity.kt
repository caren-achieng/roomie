package com.lc.roomie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_complete_profile.*
import kotlinx.android.synthetic.main.activity_houserules.*
import androidx.appcompat.app.AppCompatDelegate


class CompleteProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        continueProfile.setOnClickListener{
            val intent = Intent(this, PersonalityActivity::class.java)
            startActivity(intent)
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        var datepicker = findViewById<DatePicker>(R.id.birthday)
        datepicker.calendarViewShown = false
    }

    fun onRadioButtonClicked(view: View) {}
}
