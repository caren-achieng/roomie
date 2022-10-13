package com.lc.roomie


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony.MmsSms.PendingMessages
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_complete_profile.*
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
