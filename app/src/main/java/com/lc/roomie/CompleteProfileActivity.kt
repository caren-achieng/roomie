package com.lc.roomie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton


class CompleteProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)
        var datepicker = findViewById<DatePicker>(R.id.birthday)
        datepicker.calendarViewShown = false
    }

    fun onRadioButtonClicked(view: View) {}
}

//fun onRadioButtonClicked(view: View) {
//    if (view is RadioButton) {
//        // Is the button now checked?
//        val checked = view.isChecked
//
//        // Check which radio button was clicked
//        when (view.getId()) {
//            R.id.radio_male ->
//                if (checked) {
//                    // Pirates are the best
//                }
//            R.id.radio_female ->
//                if (checked) {
//                    // Ninjas rule
//                }
//        }
//    }
//}