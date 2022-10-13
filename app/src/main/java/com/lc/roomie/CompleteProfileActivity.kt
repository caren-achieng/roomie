package com.lc.roomie

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony.MmsSms.PendingMessages
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_complete_profile.*


class CompleteProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        birthday.calendarViewShown = false

        change_profile_picture.setOnClickListener {
            if (VERSION.SDK_INT >= VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                    var permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

                    requestPermissions(permissions, PERMISSION_CODE)

                }else{

                }
            }else{

            }
        }

    }

    fun onRadioButtonClicked(view: View) {}
    companion object{
        private val IMAGE_PICK_CODE = 1000;

        private val PERMISSION_CODE = 1001
    }
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