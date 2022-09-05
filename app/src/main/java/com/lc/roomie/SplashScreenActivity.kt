package com.lc.roomie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val animation = AnimationUtils.loadAnimation( this, R.anim.logoanimation)

        topTextView.startAnimation(animation)

        val splashScreenTimeOut = 4000
        val homeIntent = Intent(this@SplashScreenActivity, LoginActivity::class.java)

        Handler(Looper.getMainLooper()).postDelayed({
          startActivity(homeIntent)
            finish()

        }, splashScreenTimeOut.toLong())
    }
}