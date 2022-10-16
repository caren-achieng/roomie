package com.lc.roomie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        val animation = AnimationUtils.loadAnimation( this, R.anim.logoanimation)

        logo.startAnimation(animation)

        val splashScreenTimeOut = 4000
        val loginIntent = Intent(this@SplashScreenActivity, LoginActivity::class.java)

        //CHANGE TO PAGE YOU WANT TO MAKE
        val developIntent = Intent(this@SplashScreenActivity, PersonalityActivity::class.java)

        val user = Firebase.auth.currentUser

//        if (user != null){
//            Handler(Looper.getMainLooper()).postDelayed({
//                startActivity(homeIntent)
//                finish()
//            }, splashScreenTimeOut.toLong())
//        } else {
//            Handler(Looper.getMainLooper()).postDelayed({
//                startActivity(loginIntent)
//                finish()
//            }, splashScreenTimeOut.toLong())
//        }



        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(developIntent)
            finish()
        }, splashScreenTimeOut.toLong())

    }
}